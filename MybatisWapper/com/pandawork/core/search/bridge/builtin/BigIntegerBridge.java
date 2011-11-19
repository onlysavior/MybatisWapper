package com.pandawork.core.search.bridge.builtin;

import java.math.BigInteger;

import com.pandawork.core.util.StringHelper;

public class BigIntegerBridge extends NumberBridge {

	public Object stringToObject(String stringValue) {
		if (StringHelper.isEmpty(stringValue)) {
			return null;
		}
		return new BigInteger(stringValue);
	}

}
