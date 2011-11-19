package com.pandawork.core.search.bridge.builtin;

import com.pandawork.core.search.bridge.TwoWayStringBridge;
import com.pandawork.core.util.ReflectionHelper;
import com.pandawork.core.util.StringHelper;

public class ClassBridge implements TwoWayStringBridge {

	public Object stringToObject(String stringValue) {
		if (StringHelper.isEmpty(stringValue)) {
			return null;
		} else {
			try {
				return ReflectionHelper.classForName(stringValue,
						ClassBridge.class);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Unable to deserialize Class: "
						+ stringValue + e.getMessage());
			}
		}
	}

	public String objectToString(Object object) {
		return object == null ? null : ((Class<?>) object).getName();

	}
}
