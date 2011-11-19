package com.pandawork.core.search.bridge.builtin;

import java.math.BigDecimal;

import com.pandawork.core.util.StringHelper;


public class BigDecimalBridge extends NumberBridge {

	public Object stringToObject(String stringValue) {
		if ( StringHelper.isEmpty( stringValue ) ) return null;
		return new BigDecimal( stringValue );
	}

}
