package com.pandawork.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import com.pandawork.core.bean.StaticAutoWire;

public class SpringUtil {

    private static final Map<Class<?>, Object> beanStaticMap = new ConcurrentHashMap<Class<?>, Object>();

    private static final Object beanStaticInitLock = new Object();

    public static void beanStaticWire(Class<?> beanClazz, ApplicationContext context) {
        if (!beanStaticMap.containsKey(beanClazz)) {
            synchronized (beanStaticInitLock) {
                if (!beanStaticMap.containsKey(beanClazz)) {
                    Set<Class<?>> cls = ReflectUtil.getAllInterfacesAndSuperClass(beanClazz);
                    cls.add(beanClazz);
                    for (Class<?> clazz : cls) {
                        Field[] fields = clazz.getDeclaredFields();
                        for (Field f : fields) {
                            if (Modifier.isStatic(f.getModifiers())) {
                                if (!f.isAnnotationPresent(StaticAutoWire.class)) {
                                    continue;
                                }
                                boolean needResetAccessible = false;
                                try {
                                    if (!f.isAccessible()) {
                                        f.setAccessible(true);
                                        needResetAccessible = true;
                                    }

                                    Qualifier qa = f.getAnnotation(Qualifier.class);
                                    Object bean = (qa == null ? BeanFactoryUtils.beanOfType(
                                            context, f.getType()) : context.getBean(qa.value()));

                                    f.set(null, bean);
                                } catch (IllegalArgumentException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } finally {
                                    if (needResetAccessible) {
                                        f.setAccessible(false);
                                    }
                                }
                            }
                        }
                    }
                    beanStaticMap.put(beanClazz, "");
                }
            }
        }
    }
}
