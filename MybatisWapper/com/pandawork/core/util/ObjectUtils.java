package com.pandawork.core.util;

/*
 * Copyright 2002-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.lang.reflect.Array;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Miscellaneous object utility methods. Mainly for internal use within the framework; consider Jakarta's Commons Lang for a more comprehensive suite of object utilities.
 * 
 * @author Juergen Hoeller
 * @author Keith Donald
 * @author Rod Johnson
 * @author Rob Harrop
 * @author Alex Ruiz
 * @since 19.03.2004
 * @see org.apache.commons.lang.ObjectUtils
 */
public class ObjectUtils {
	/**
	 * 判断一个对象是不是数组
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isArray(Object obj) {
		return ((obj != null) && obj.getClass().isArray());
	}

	/**
	 * 把一个身份不明的疑似数组的对象搞成一个数组.
	 * 
	 * @param source 如果参数为null,会返回一个0长度的Object[]
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] toObjectArray(Object source) {
		if (source instanceof Object[]) {
			return (T[]) source;
		}
		if (source == null) {
			return (T[]) new Object[0];
		}
		if (!source.getClass().isArray()) {
			throw new IllegalArgumentException("Source is not an array: " + source);
		}
		int length = Array.getLength(source);
		if (length == 0) {
			return (T[]) new Object[0];
		}
		Object first = Array.get(source, 0);
		Class<?> wrapperType = first.getClass();
		Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
		newArray[0] = first;
		for (int i = 1; i < length; i++) {
			newArray[i] = Array.get(source, i);
		}
		return (T[]) newArray;
	}

	/**
	 * Return a content-based String representation if <code>obj</code> is not <code>null</code>; otherwise returns an empty String.
	 * <p>
	 * Differs from {@link #nullSafeToString(Object)} in that it returns an empty String rather than "null" for a <code>null</code> value.
	 * 
	 * @param obj the object to build a display String for
	 * @return a display String representation of <code>obj</code>
	 * @see #nullSafeToString(Object)
	 */
	public static String javaBeanToString(Object obj) {
		try {
			Map<?, ?> map = PropertyUtils.describe(obj);
			return map.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "null";
	}

}
