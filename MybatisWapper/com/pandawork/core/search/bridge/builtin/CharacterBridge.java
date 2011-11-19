package com.pandawork.core.search.bridge.builtin;

import com.pandawork.core.search.bridge.TwoWayStringBridge;
import com.pandawork.core.util.StringHelper;

public class CharacterBridge implements TwoWayStringBridge {

	public Object stringToObject(String stringValue) {
		if (StringHelper.isEmpty(stringValue)) {
			return null;
		}
		if (stringValue.length() > 1) {
			throw new IllegalArgumentException("<" + stringValue
					+ "> is not a char");
		}
		return stringValue.charAt(0);
	}

	public String objectToString(Object object) {
		return object == null ? null : object.toString();
	}

}
