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
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.util.EventTimer;


public class IWBundleStarter implements IWBundleStartable, ActionListener {

	private EventTimer timer;
	public static final String IW_WEATHER_TIMER = "iw_weather_timer";
	public static final String URL = "http://beljandi.vedur.is/serthj/askrift/ath?notandi=laddi&lykilord=bakki";

	public void start(IWBundle starterBundle) {
		timer = new EventTimer(EventTimer.THREAD_SLEEP_24_HOURS/6,IW_WEATHER_TIMER);
		timer.addActionListener(this);
		timer.start();
	}

	public void stop(IWBundle starterBundle) {
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equalsIgnoreCase(IW_WEATHER_TIMER)) {
			WeatherBusiness business = IcelandicWeatherBusiness.getInstance();
			business.parseXML(URL);
			System.out.println("Done parsing weather data...");
		}
	}
}