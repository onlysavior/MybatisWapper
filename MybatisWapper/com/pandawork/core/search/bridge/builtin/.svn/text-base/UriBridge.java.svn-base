package com.pandawork.core.search.bridge.builtin;

import java.net.URI;
import java.net.URISyntaxException;

import com.pandawork.core.search.bridge.TwoWayStringBridge;
import com.pandawork.core.util.StringHelper;

public class UriBridge implements TwoWayStringBridge {

	public Object stringToObject(String stringValue) {
		if (StringHelper.isEmpty(stringValue)) {
			return null;
		} else {
			try {
				return new URI(stringValue);
			} catch (URISyntaxException e) {
				throw new RuntimeException("Unable to build URI: "
						+ stringValue, e);
			}
		}
	}

	public String objectToString(Object object) {
		return object == null ? null : object.toString();
	}

}
