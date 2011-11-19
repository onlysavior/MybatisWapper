package com.pandawork.core.dao.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * 域的pair
 * 
 * @author Lionel pang
 * @date 2011-9-14
 *
 */
public class FieldPair {

    // 域
    private Field field;

    // 描述
    private PropertyDescriptor pd;

    // 类名称
    private Class<?> cla;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public PropertyDescriptor getPd() {
        return pd;
    }

    public void setPd(PropertyDescriptor pd) {
        this.pd = pd;
    }

    public Class<?> getCla() {
        return cla;
    }

    public void setCla(Class<?> cla) {
        this.cla = cla;
    }

}
