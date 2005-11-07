/*
 * $Id$
 * Created on Nov 4, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.weather.presentation;

import is.idega.block.weather.business.IcelandicWeatherBusiness;
import com.idega.block.weather.business.WeatherBusiness;
import com.idega.block.weather.presentation.AbstractWeather;


public class IcelandicWeather extends AbstractWeather {

	protected WeatherBusiness getBusiness() {
		return IcelandicWeatherBusiness.getInstance();
	}
}
