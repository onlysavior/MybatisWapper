package com.pandawork.core.search.bridge.builtin;

import com.pandawork.core.search.bridge.TwoWayStringBridge;
import com.pandawork.core.util.StringHelper;

public class BooleanBridge implements TwoWayStringBridge {

	public Boolean stringToObject(String stringValue) {
		if (StringHelper.isEmpty(stringValue))
			return null;
		return Boolean.valueOf(stringValue);
	}

	public String objectToString(Object object) {
		return object == null ? null : object.toString();
	}

}
