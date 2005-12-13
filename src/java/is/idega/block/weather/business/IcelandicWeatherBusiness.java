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
			Collection children = rootElement.getChildren("Stod");
			Iterator iter = children.iterator();
			while (iter.hasNext()) {
				XMLElement element = (XMLElement) iter.next();
				String name = element.getAttribute("nafn").getValue();
				String id = element.getAttribute("wmonr").getValue();
				
				XMLElement data = element.getChild("Athugun");
				String time = data.getAttribute("timi").getValue();
				
				WeatherData weather = getWeather(id);
				if (weather == null) {
					weather = new WeatherData();
				}
				
				weather.setID(id);
				weather.setName(name);
				weather.setTimestamp(new IWTimestamp(time).getTimestamp());

				String temperature = data.getChild("Hitastig").getValue();
				String windspeed = data.getChild("Vindstyrkur").getValue();
				
				weather.setTemperature(new Float(temperature));
				weather.setWindspeed(new Float(windspeed));

				if (data.getChild("Vindatt") != null) {
					String windDirection = data.getChild("Vindatt").getValue();
					String windDirectionTxt = data.getChild("VindattTxt").getValue();

					weather.setWindDirection(new Float(windDirection));
					weather.setWindDirectionTxt(windDirectionTxt);
				}
				
				if (data.getChild("Skyjahula") != null) {
					String code = data.getChild("Vedur").getAttribute("kodi").getValue();
					String codeURL = data.getChild("Vedur").getAttribute("url").getValue();
					String description = data.getChild("Vedur").getValue();
					String clearance = data.getChild("Skyggni").getValue();

					weather.setWeatherDescription(description);
					weather.setClearance(clearance);
					weather.setWeatherCode(code);
					weather.setWeatherCodeURL(codeURL);
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