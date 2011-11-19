package com.pandawork.core.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * 通用默认值检测
 * 
 * @author Lionel pang
 * @date 2010-4-17
 */
public class CommonDefaultCheck {

    /**
     * 默认collection设置
     * 如果collection为null,则返回默认的collection
     * 
     * @param collection
     * @return
     */
    public static Collection<?> checkDefault(Collection<?> collection) {
        return collection == null ? Collections.emptyList() : collection;
    }
    
    /**
     * 默认map对象进行检测
     * 如果map == null，则返回默认的map
     * 
     * @param map
     * @return
     */
    public static Map<?, ?> checkDefault(Map<?, ?> map){
        return map == null? Collections.emptyMap(): map;
    }

    /**
     * 默认set对象进行检测
     * 如果set为null，则返回默认set
     * 
     * @param set
     * @return
     */
    public static Set<?> checkDefault(Set<?> set){
        return set == null? Collections.emptySet(): set;
    }
    
    /**
     * 默认检查Integer对象，
     * 如果integer为null,则返回0
     * @param i
     * @return
     */
    public static Integer checkDefault(Integer i){
        return i == null? 0: i;
    }
}
