package com.pandawork.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

/**
 * ....T_T.....
 * 
 * @author Administrator
 * 
 */
public abstract class ReflectionHelper {

	public static Object getMemberValue(Object entity, Member member) {
		if (entity == null || member == null) {
			throw new IllegalArgumentException("参数错误，都不能为空");
		}

		if (member instanceof Field) {
			Field field = (Field) member;
			field.setAccessible(true);
			try {
				return field.get(entity);
			} catch (Exception e) {
				return null;
			}
		} else if (member instanceof Method) {
			Method method = (Method) member;
			method.setAccessible(true);

			try {
				return method.invoke(entity, null);
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public static Class classForName(String name) throws ClassNotFoundException {
		try {
			ClassLoader contextClassLoader = Thread.currentThread()
					.getContextClassLoader();
			if (contextClassLoader != null) {
				return contextClassLoader.loadClass(name);
			}
		} catch (Throwable t) {
		}
		return Class.forName(name);
	}

	@SuppressWarnings("rawtypes")
	public static <T> Class classForName(String name, Class<T> caller)
			throws ClassNotFoundException {
		try {
			ClassLoader contextClassLoader = Thread.currentThread()
					.getContextClassLoader();
			if (contextClassLoader != null) {
				return contextClassLoader.loadClass(name);
			}
		} catch (Throwable e) {
		}
		return Class.forName(name, true, caller.getClassLoader());
	}

	public static String getAttributeName(Member member, String name) {
		return StringHelper.isEmpty(name) ? name : member.getName();
	}

	public static void setAccessible(Member member) {
		if (member instanceof Field) {
			((Field) member).setAccessible(true);
		} else if (member instanceof Method) {
			((Method) member).setAccessible(true);
		} else {
			throw new RuntimeException("不能设为public 不知道的类型");
		}
	}
}
