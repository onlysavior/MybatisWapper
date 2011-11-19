package com.pandawork.core.dao.impl;

/**
 * 域以及域名的key和value值
 * 
 * @author Lionel pang
 * @date 2011-9-14
 * 
 */
public class KeyValuePair {

    private Object key;

    private Object Value;

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getValue() {
        return Value;
    }

    public void setValue(Object value) {
        Value = value;
    }

}
