/*
 * $Id$
 * Created on Nov 4, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.weather;

import is.idega.block.weather.business.IcelandicWeatherBusiness;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.idega.block.weather.business.WeatherBusiness;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.servlet.filter.IWBundleResourceFilter;
import com.idega.util.EventTimer;


public class IWBundleStarter implements IWBundleStartable, ActionListener {

	private EventTimer timer;
	public static final String IW_WEATHER_TIMER = "iw_weather_timer_is";
	private String URL = "http://xmlweather.vedur.is/?op_w=xml&type=obs&lang=is&anytime=1&time=3h&view=xml&params=V;D;F;W;T&ids=";

	public void start(IWBundle starterBundle) {
		IWBundleResourceFilter.copyAllFilesFromJarDirectory(starterBundle.getApplication(), starterBundle, "/resources/");

		setURL(starterBundle.getApplication().getIWApplicationContext());
		this.timer = new EventTimer(EventTimer.THREAD_SLEEP_24_HOURS / 8,IW_WEATHER_TIMER);
		this.timer.addActionListener(this);
		this.timer.start();
	}

	/**
	 * <p>
	 * Get the URL to the weather information.
	 * </p>
	 */
	private void setURL(IWApplicationContext iwac) {
		String KEY = "weather.url.is";
		String IDS = "weather.stations.is";
		
		String xmlURL = iwac.getApplicationSettings().getProperty(KEY);
		if (xmlURL == null) {
			iwac.getApplicationSettings().setProperty(KEY, this.URL);
			xmlURL = this.URL;
		}
		
		String ids = iwac.getApplicationSettings().getProperty(IDS);
		if (ids == null) {
			iwac.getApplicationSettings().setProperty(IDS, "1");
			ids = "1";
		}
		
		this.URL = xmlURL + ids;
	}

	public void stop(IWBundle starterBundle) {
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equalsIgnoreCase(IW_WEATHER_TIMER)) {
			WeatherBusiness business = IcelandicWeatherBusiness.getInstance();
			business.parseXML(this.URL);
			System.out.println("Done parsing weather data...");
		}
	}
}