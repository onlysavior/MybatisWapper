package com.pandawork.core.dao.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.pandawork.core.entity.AbstractEntity;

/**
 * commondao的工具操作类
 * 
 * @author Lionel pang
 * @date 2011-9-14
 * 
 */
public class CommonDaoUtil {

	// 逐渐缓存
	private static Map<Class<?>, FieldPair> primKeyCache = new ConcurrentHashMap<Class<?>, FieldPair>();

	// 域缓存
	private static Map<Class<?>, List<FieldPair>> fieldCache = new ConcurrentHashMap<Class<?>, List<FieldPair>>();

	// 表名缓存
	private static Map<Class<?>, String> tableCache = new ConcurrentHashMap<Class<?>, String>();

	// 查询出主键域
	public static FieldPair getPrimKey(Object obj, Class<?> cla)
			throws Exception {
		FieldPair key = primKeyCache.get(cla);
		if (key == null) {
			List<FieldPair> pairs = listField(obj, cla);
			for (FieldPair pair : pairs) {
				Field field = pair.getField();
				Id id = field.getAnnotation(Id.class);
				if (id != null) {
					key = pair;
					primKeyCache.put(cla, key);
					break;
				}
			}
		}

		return key;
	}

	// 返回所有的域描述符
	public static List<FieldPair> listField(Object obj, Class<?> cla)
			throws Exception {
		List<FieldPair> fields = fieldCache.get(cla);
		if (fields == null) {
			if (!fieldCache.containsKey(cla)) {
				PropertyDescriptor[] tmpFields = PropertyUtils
						.getPropertyDescriptors(cla);

				String[] escaps = new String[0];
				if (obj instanceof AbstractEntity) {
					escaps = ((AbstractEntity) obj).escap();
				}

				boolean isEscap = false;
				fields = new ArrayList<FieldPair>();
				for (PropertyDescriptor pd : tmpFields) {
					isEscap = false; // 是否已经被过滤.true表示已经被过滤，false表示没有被过滤。
					for (String escap : escaps) {
						if (escap.equals(pd.getName())) {
							isEscap = true;
						}
					}

					if (isEscap) { // 如果出现已经被过滤，则直接进入下一个域
						continue;
					}

					FieldPair pair = new FieldPair();
					pair.setCla(cla);
					pair.setPd(pd);
					pair.setField(cla.getDeclaredField(pd.getName()));

					fields.add(pair);
				}

				fieldCache.put(cla, fields);
			}

			fields = fieldCache.get(cla);
		}

		return fields;
	}

	/**
	 * 读取数据
	 * 
	 * @param bean
	 * @param pair
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object readValue(Object bean, FieldPair pair)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Method method = pair.getPd().getReadMethod();
		if (method == null) {
			return null;
		}

		return method.invoke(bean);
	}

	/**
	 * 获取读方法
	 * 
	 * @param cla
	 * @param pair
	 * @return
	 */
	public static Method getReadMethod(Class<?> cla, FieldPair pair) {
		Method readMethod = pair.getPd().getReadMethod();
		return readMethod;
	}

	/**
	 * 获取表明
	 * 
	 * @param cla
	 * @return
	 */
	public static String getTableName(Class<?> cla) {
		String table = tableCache.get(cla);
		if (table == null) {
			Table tab = cla.getAnnotation(Table.class);
			if (tab == null) {
				return null;
			}

			table = tab.name();
			tableCache.put(cla, table);
		}

		return table;
	}

	/**
	 * 由查询出的Map转换到对应的实体
	 * 
	 * @param <T>
	 * @param clazz
	 * @param map
	 * @return
	 */
	public static <T> T convertMap2Object(Class<T> clazz,Map<String, Object> map) {
		T obj = null;
		try {
			 obj = clazz.newInstance();
			 BeanUtils.populate(obj, map);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return obj;
	}

	/**
	 * 由查询出的List<Map> 转换到相应的List<T>
	 * 
	 * @param <T>
	 * @param clazz
	 * @param listMap
	 * @return
	 */
	public static <T> List<T> convertListMap2ListObject(Class<T> clazz,
			List<Map<String, Object>> listMap) {
		List<T> result = new ArrayList<T>();
		for (Map<String, Object> tempMap : listMap) {
			result.add(convertMap2Object(clazz, tempMap));
		}
		return result.size() > 0 ? result : null;
	}

	/**
	 * 得到一个实体中Field的名字与值，以KeyValuePair的形式;如果properties为空，则默认获得除id属性外的所有属性
	 * 如果properties不为空，则得到properties中的属性（id 除外）
	 * 
	 * isMerge 作为判断merge的开关，区别在于List<KeyValuePair>中是否包括id;isMerge在merge时为true
	 * 
	 * 
	 * @param obj
	 * @param cla
	 * @param isMerge
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static List<KeyValuePair> listKeyValuePair(Object obj, Class<?> cla,Boolean isMerge,
			String... properties) throws Exception {
		boolean propertiesEmpty = true;
		List<String> propertyList = null;
		if (properties != null && properties.length > 0) {
			propertiesEmpty = false;
			propertyList = Arrays.asList(properties);
		}
		FieldPair idPair = getPrimKey(obj, cla);
		String idName = idPair.getField().getName();

		List<FieldPair> fields = listField(obj, cla);
		List<KeyValuePair> keyValueList = new ArrayList<KeyValuePair>();

		KeyValuePair tempKeyValuePair;
		for (FieldPair f : fields) {
			tempKeyValuePair = new KeyValuePair();
			String fieldName = f.getField().getName();
			if (fieldName.equals(idName) && !isMerge) {
				continue;
			}
			if (!propertiesEmpty && !propertyList.contains(fieldName)) {
				continue;
			}
			tempKeyValuePair.setKey(fieldName);
			tempKeyValuePair.setValue(CommonDaoUtil.readValue((Object) obj, f));
			keyValueList.add(tempKeyValuePair);
		}

		return keyValueList.size() > 0 ? keyValueList : null;
	}
	
	public static List<String> listFieldNamesExcludeGiven(Class<?> clazz,List<String> excludeFieldList){
		Field[] fields = clazz.getDeclaredFields();
		
		List<String> resultList = new ArrayList<String>();
		for(Field field :fields){
			if(excludeFieldList.contains(field.getName()) || field.getName().equals("serialVersionUID")){
				continue;
			}
			resultList.add(field.getName());
		}
		return resultList;
	}

	/**
	 * 得到一个实体中Field的名字与值，以KeyValuePair的形式
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static List<KeyValuePair> listKeyValuePair(Object obj)
			throws Exception {
		return listKeyValuePair(obj, obj.getClass(),false);
	}

	/**
	 * 得到一个实体中Field的名字与值，以KeyValuePair的形式
	 * 
	 * @param obj
	 * @param cla
	 * @return
	 * @throws Exception
	 */
	public static List<KeyValuePair> listKeyVluePair(Object obj, Class<?> cla)
			throws Exception {
		return listKeyValuePair(obj, cla,false);
	}
	
	/**
	 * 给定一个类，找到类中主键的name
	 * @param <T>
	 * @param cla
	 * @return
	 */
	public static<T> String getPrimKeyName(Class<T> cla){
		Field[] fields = cla.getDeclaredFields();
		for(Field field : fields){
			Id id = field.getAnnotation(Id.class);
			if(id != null)
				return field.getName();
		}
		return null;
	}
}
