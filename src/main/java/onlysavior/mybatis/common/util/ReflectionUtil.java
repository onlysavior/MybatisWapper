package onlysavior.mybatis.common.util;


import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-7
 * Time: 下午3:41
 * To change this template use File | Settings | File Templates.
 */
public class ReflectionUtil {

    public static Object readValue(Object target, String propertyName) {
        String getMethodName = "get" + StringUtils.capitalize(propertyName);
        Method getMethod = null;
        try {
            getMethod = target.getClass().getMethod(getMethodName,null);
            return getMethod.invoke(target,null);
        } catch (NoSuchMethodException ignored) {
        } catch (InvocationTargetException e) {
        } catch (IllegalAccessException e) {
        }

        try {
            Field field = target.getClass().getField(propertyName);
            boolean change = false;
            if (!field.isAccessible()) {
                change = true;
                field.setAccessible(true);
            }
            Object value = field.get(target);
            if(change) {
                field.setAccessible(false);
            }

            return value;
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }

        return null;
    }

    public static List<Pair> listKeyValuePair(Object obj) {
        return listKeyValuePair(obj, true);
    }

    public static List<Pair> listKeyValuePair(Object obj, boolean excludeEmpty) {
        List<Pair> rtn = new LinkedList<Pair>();
        Field[] fields = obj.getClass().getDeclaredFields();
        Pair tmp;
        for(Field field : fields) {
            tmp = new Pair();
            tmp.setKey(field.getName());

            Object value = readValue(obj, field.getName());
            if(excludeEmpty && value == null) {
                continue;
            }
            tmp.setValue(value);
            rtn.add(tmp);
        }

        return rtn.size() > 0 ? rtn : null;
    }

    public static List<Pair> listKeyValuePair(Object obj, boolean excludeEmpty, String... includes) {
        List<Pair> rtn = new LinkedList<Pair>();
        List<Field> fields = new ArrayList<Field>(includes.length);
        for(String i : includes) {
            try {
                Field field = obj.getClass().getDeclaredField(i);
                fields.add(field);
            } catch (NoSuchFieldException e) {
                continue;
            }
        }

        Pair tmp;
        for(Field field : fields) {
            tmp = new Pair();
            tmp.setKey(field.getName());

            Object value = readValue(obj, field.getName());
            if(excludeEmpty && value == null) {
                continue;
            }
            tmp.setValue(value);
            rtn.add(tmp);
        }

        return rtn.size() > 0 ? rtn : null;
    }

    public static <T> T convertMap2Object(Class<T> clazz, Map<String, Object> map) {
        T obj = null;
        try {
            obj = clazz.newInstance();
            BeanUtils.populate(obj, map);

        } catch (Exception e) {
            return null;
        }
        return obj;
    }

    public static <T> List<T> convertListMap2ListObject(Class<T> clazz,
                                                        List<Map<String, Object>> listMap) {
        List<T> result = new LinkedList<T>();
        for (Map<String, Object> tempMap : listMap) {
            result.add(convertMap2Object(clazz, tempMap));
        }
        return result.size() > 0 ? result : null;
    }
}

