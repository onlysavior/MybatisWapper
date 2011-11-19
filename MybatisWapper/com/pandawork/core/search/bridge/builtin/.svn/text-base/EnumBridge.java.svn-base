package com.pandawork.core.search.bridge.builtin;

import com.pandawork.core.search.bridge.TwoWayStringBridge;
import com.pandawork.core.util.StringHelper;

public class EnumBridge implements TwoWayStringBridge {
	
	@SuppressWarnings("rawtypes")
	private Class<? extends Enum> clazz = null;

	@SuppressWarnings("rawtypes")
	public String objectToString(Object object) {
		Enum e = (Enum) object;
		return e != null ? e.name() : null;
	}

	@SuppressWarnings("rawtypes")
	public void setAppliedOnType(Class<?> returnType) {
		@SuppressWarnings("unchecked") //only called for an enum
		Class<? extends Enum> enumReturnType = (Class<? extends Enum>) returnType;
		this.clazz = enumReturnType;
	}
	
	@SuppressWarnings("unchecked")
	public Enum<? extends Enum> stringToObject(String stringValue) {
		if ( StringHelper.isEmpty( stringValue ) ) return null;
		return Enum.valueOf( clazz, stringValue );
	}

}
