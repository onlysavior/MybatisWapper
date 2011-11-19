package com.pandawork.core.search.bridge.builtin;

import com.pandawork.core.search.bridge.TwoWayStringBridge;

public abstract class NumberBridge implements TwoWayStringBridge {

	public String objectToString(Object object) {
		return object != null ? object.toString() : null;
	}
}
