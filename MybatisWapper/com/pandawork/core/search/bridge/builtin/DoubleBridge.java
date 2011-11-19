package com.pandawork.core.search.bridge.builtin;

import com.pandawork.core.util.StringHelper;

public class DoubleBridge extends NumberBridge {

	public Object stringToObject(String stringValue) {
		if (StringHelper.isEmpty(stringValue))
			return null;
		return Double.valueOf(stringValue);
	}

}
