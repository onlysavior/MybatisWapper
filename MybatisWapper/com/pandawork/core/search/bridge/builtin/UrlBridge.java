package com.pandawork.core.search.bridge.builtin;

import java.net.MalformedURLException;
import java.net.URL;

import com.pandawork.core.search.bridge.TwoWayStringBridge;
import com.pandawork.core.util.StringHelper;

public class UrlBridge implements TwoWayStringBridge {

	public Object stringToObject(String stringValue) {
		if (StringHelper.isEmpty(stringValue)) {
			return null;
		} else {
			try {
				return new URL(stringValue);
			} catch (MalformedURLException e) {
				throw new RuntimeException("Unable to build URL: "
						+ stringValue, e);
			}
		}
	}

	public String objectToString(Object object) {
		return object == null ? null : object.toString();
	}

}
