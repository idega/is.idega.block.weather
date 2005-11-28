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
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.block.weather.business.WeatherBusiness;
import com.idega.core.data.ICApplicationBinding;
import com.idega.core.data.ICApplicationBindingHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.util.EventTimer;


public class IWBundleStarter implements IWBundleStartable, ActionListener {

	private EventTimer timer;
	public static final String IW_WEATHER_TIMER = "iw_weather_timer";
	private String URL = "http://beljandi.vedur.is/serthj/askrift/ath?notandi=laddi&lykilord=bakki";

	public void start(IWBundle starterBundle) {
		setURL();
		timer = new EventTimer(EventTimer.THREAD_SLEEP_24_HOURS/6,IW_WEATHER_TIMER);
		timer.addActionListener(this);
		timer.start();
	}

	/**
	 * <p>
	 * Get the url to the weather information. Searches the ICApplicationBinding db table.
	 * </p>
	 */
	private void setURL() {
		try {
			String KEY = "ICELANDIC_WEATHER_URL";
			ICApplicationBindingHome ibHome = (ICApplicationBindingHome) IDOLookup.getHome(ICApplicationBinding.class);
			try {
				ICApplicationBinding ab = ibHome.findByPrimaryKey(KEY);
				URL = ab.getValue();
			}
			catch (FinderException e) {
				try {
					ICApplicationBinding ab = ibHome.create();
					ab.setKey(KEY);
					ab.setValue(URL);
					ab.setBindingType("weather_block_binding");
					ab.store();
				}
				catch (CreateException e1) {
					e1.printStackTrace();
				}
			}
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
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