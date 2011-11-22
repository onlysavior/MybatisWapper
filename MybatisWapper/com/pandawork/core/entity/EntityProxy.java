package com.pandawork.core.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.StringUtils;

import com.pandawork.core.dao.CommonDao;

public class EntityProxy<T> implements MethodInterceptor {
	
	private static ThreadLocal<CommonDao> commonDao;
	private T originEntity;
	private final List<String> exculdeFieldList;
	
	
	public EntityProxy(List<String> exFieldNameList){
		exculdeFieldList = exFieldNameList; 
		for(String propertyName : exculdeFieldList){
			propertyName = propertyName.toLowerCase();
		}
	}

	@SuppressWarnings("unchecked")
	public T getEntityProxy(T obj){
		originEntity = obj;
		Class<?> clazz = obj.getClass();
		
		Enhancer e = new Enhancer();
		e.setSuperclass(clazz);
		e.setCallback(this);
		
		T result =  (T) e.create();
		try {
			BeanUtils.copyProperties(result, originEntity);
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	@Override
	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		Object returnValue = null;
		String methodName = null;
		methodName = method.getName();
		if(methodName.startsWith("set")){
			returnValue = proxy.invokeSuper(obj, args);
		}else if(methodName.startsWith("get")){
			String propertyName = methodName.substring(3).toLowerCase();
			if(!exculdeFieldList.contains(propertyName)){
				returnValue = proxy.invokeSuper(obj, args);
			}else{
				try{
					returnValue = ((CommonDao) commonDao).lazyLoad(originEntity,StringUtils.uncapitalize(propertyName));
					
					BeanUtils.setProperty(obj, propertyName, returnValue);
					BeanUtils.setProperty(originEntity, propertyName, returnValue);
					exculdeFieldList.remove(propertyName);
				}catch(Exception e){
					System.out.println(e);
				}
					
			}
		}
		return returnValue;
	}

	public static void setCommonDao(CommonDao dao){
		if(commonDao == null){
			commonDao = new ThreadLocal<CommonDao>();
			commonDao.set(dao);
		}
	}

	public void setOriginEntity(T originEntity) {
		this.originEntity = originEntity;
	}
}
