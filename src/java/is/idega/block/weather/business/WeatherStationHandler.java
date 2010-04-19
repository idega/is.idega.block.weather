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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import com.idega.block.weather.business.WeatherData;
import com.idega.core.builder.presentation.ICPropertyHandler;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;


public class WeatherStationHandler implements ICPropertyHandler {

	public List getDefaultHandlerTypes() {
		return null;
	}

	public PresentationObject getHandlerObject(String name, String stringValue, IWContext iwc) {
		DropdownMenu menu = new DropdownMenu(name);
		
		Collection stations = IcelandicWeatherBusiness.getInstance().getAllWeatherData();
		Iterator iter = stations.iterator();
		while (iter.hasNext()) {
			WeatherData data = (WeatherData) iter.next();
			menu.addMenuElement(data.getID(), data.getName() + " (" + data.getID() + ")");
		}
		if (stringValue != null) {
			menu.setSelectedElement(stringValue);
		}
		
		return menu;
	}

	public void onUpdate(String[] values, IWContext iwc) {
	}

	public PresentationObject getHandlerObject(String name, String stringValue,
			IWContext iwc, boolean oldGenerationHandler, String instanceId,
			String method) {
		// TODO Auto-generated method stub
		return null;
	}
}
