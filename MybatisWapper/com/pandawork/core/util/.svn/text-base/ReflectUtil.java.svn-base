package com.pandawork.core.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ClassUtils;

import com.pandawork.core.bean.PropertyDesc;

public class ReflectUtil {

	private static final ConcurrentHashMap<Class<?>, PropertyDesc[]> propertyDescCache = new ConcurrentHashMap<Class<?>, PropertyDesc[]>();
	private static final ConcurrentHashMap<String, PropertyDesc> propertyDescSingleCache = new ConcurrentHashMap<String, PropertyDesc>();
	private static final ConcurrentHashMap<String, Method> methodCache = new ConcurrentHashMap<String, Method>();

	public static PropertyDesc[] getPropertyDescs(Class<?> clazz) {
		PropertyDesc[] ps = propertyDescCache.get(clazz);
		if (ps == null) {
			PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(clazz);
			ps = new PropertyDesc[pds.length];
			for (int i = 0; i < pds.length; i++) {
				PropertyDescriptor pd = pds[i];
				PropertyDesc p = new PropertyDesc();
				p.setName(StringUtils.uncapitalize(pd.getName()));
				p.setPropertyType(pd.getPropertyType());
				p.setReadMethod(pd.getReadMethod());
				p.setWriteMethod(pd.getWriteMethod());
				ps[i] = p;
			}
			propertyDescCache.putIfAbsent(clazz, ps);
		}
		return ps;
	}
	
	public static PropertyDesc getPropertyDesc(Class<?> clazz, String name) {
		String key = clazz.getName() + "." + name;
		PropertyDesc pd = propertyDescSingleCache.get(key);
		if(pd == null) {
			PropertyDesc[] ps = getPropertyDescs(clazz);
			for(PropertyDesc tmp : ps) {
				if(name.equals(tmp.getName())) {
					pd = tmp;
				}
			}
			if(pd != null) {
				propertyDescSingleCache.putIfAbsent(key, pd);
			} else {
				propertyDescSingleCache.putIfAbsent(key, PropertyDesc.NULL);
			}
			
		} else if(pd == PropertyDesc.NULL) {
			pd = null;
		}
		return pd;
	}

	public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws SecurityException, NoSuchMethodException {
		StringBuilder sb = new StringBuilder();
		sb.append(clazz.getName()).append(".").append(methodName);
		if (parameterTypes != null) {
			for (Class<?> pcls : parameterTypes) {
				sb.append(".").append(pcls.getName());
			}
		}
		String key = sb.toString();
		Method method = methodCache.get(key);
		if (method == null) {
			method = clazz.getMethod(methodName, parameterTypes);
			if (method != null) {
				methodCache.putIfAbsent(key, method);
			}
		}
		return method;
	}

	public static boolean isClassImplements(Class<?> targetClazz, Class<?> interfaceClazz) {
		Class<?>[] interfaces = ClassUtils.getAllInterfacesForClass(targetClazz);
		for (Class<?> clazz : interfaces) {
			if (clazz == interfaceClazz) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isClassExtends(Class<?> targetClazz, Class<?> superClazz) {
		Class<?> clazz = targetClazz.getSuperclass();
		while(clazz != null) {
			if(clazz == superClazz) {
				return true;
			}
			clazz = clazz.getSuperclass();
		}
		return false;
	}
	
	public static boolean isClassImplementsOrExtends(Class<?> targetClazz, Class<?> superClazz) {
		return isClassImplements(targetClazz, superClazz) || isClassExtends(targetClazz, superClazz);
	}
	
	public static Set<Class<?>> getAllInterfacesAndSuperClass(Class<?> targetClazz) {
		Set<Class<?>> cls = new HashSet<Class<?>>();
		Class<?>[] interfaces = ClassUtils.getAllInterfacesForClass(targetClazz);
		for (Class<?> clazz : interfaces) {
			cls.add(clazz);
		}
		Class<?> clazz = targetClazz.getSuperclass();
		while(clazz != null) {
			cls.add(clazz);
			clazz = clazz.getSuperclass();
		}
		return cls;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
	}

}
