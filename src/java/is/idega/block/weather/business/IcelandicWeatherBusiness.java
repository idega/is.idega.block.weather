/*
 * $Id$
 * Created on Nov 4, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.weather.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.idega.block.weather.business.WeatherBusiness;
import com.idega.block.weather.business.WeatherConstants;
import com.idega.block.weather.business.WeatherData;
import com.idega.util.IWTimestamp;
import com.idega.util.StringHandler;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLException;
import com.idega.xml.XMLParser;

public class IcelandicWeatherBusiness implements WeatherBusiness {

	private static IcelandicWeatherBusiness instance;
	private static Map weatherMap;

	private IcelandicWeatherBusiness() {
	}

	public static IcelandicWeatherBusiness getInstance() {
		if (instance == null) {
			instance = new IcelandicWeatherBusiness();
		}
		return instance;
	}
	
	public static void main(String[] args) {
		String URL = "http://xmlweather.vedur.is/?op_w=xml&type=obs&lang=is&anytime=1&time=3h&view=xml&params=V;D;F;W;T;FX;FG;N;P;RH;SNC;SND;SED;RTE;TD;R&ids=1";
		
		Map map = getInstance().parseXML(URL);
		System.out.println(map);
	}

	public Map parseXML(String URL) {
		XMLParser parser = new XMLParser();
		XMLElement rootElement = null;
		try {
			rootElement = parser.parse(URL).getRootElement();
		}
		catch (XMLException e) {
			e.printStackTrace(System.err);
			rootElement = null;
		}

		if (rootElement != null) {
			Collection children = rootElement.getChildren("station");
			Iterator iter = children.iterator();
			while (iter.hasNext()) {
				XMLElement element = (XMLElement) iter.next();
				
				String name = element.getChild("name").getValue();
				String id = element.getAttribute("id").getValue();

				String time = element.getChild("time").getValue();

				WeatherData weather = getWeather(id);
				if (weather == null) {
					weather = new WeatherData();
				}

				weather.setID(id);
				weather.setName(name);
				weather.setTimestamp(new IWTimestamp(time).getTimestamp());

				String temperature = element.getChild("T").getValue();
				weather.setTemperature(new Float(temperature.replaceAll(",", ".")));

				String windspeed = element.getChild("F").getValue();
				weather.setWindspeed(new Float(windspeed.replaceAll(",", ".")));

				String d = element.getChild("D").getValue();
				if (d != null && d.length() > 0) {
					String windDirection = d;
					weather.setWindDirection(windDirection);
				}

				String w = element.getChild("W").getValue();
				if (w != null && w.length() > 0) {
					String description = w;
					weather.setWeatherDescription(description);
					weather.setWeatherCode(StringHandler.stripNonRomanCharacters(description).toLowerCase());
				}

				String v = element.getChild("V").getValue();
				if (v != null && v.length() > 0) {
					String clearance = v;
					weather.setClearance(clearance);
				}

				if (weatherMap == null) {
					weatherMap = new HashMap();
				}
				weatherMap.put(id, weather);
			}
		}

		return weatherMap;
	}

	public WeatherData getWeather(String id) {
		if (weatherMap != null) {
			return (WeatherData) weatherMap.get(id);
		}
		return null;
	}

	public Collection getAllWeatherData() {
		if (weatherMap != null) {
			return weatherMap.values();
		}
		return new ArrayList();
	}

	public Collection getWeatherStations() {
		return getAllWeatherData();
	}

	public String getTemperatureSign() {
		return WeatherConstants.CELCIUS;
	}

	public String getWindSpeedUnit() {
		return WeatherConstants.METERS_PER_SECOND;
	}
}