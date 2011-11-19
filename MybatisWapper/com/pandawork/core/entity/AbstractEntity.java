package com.pandawork.core.entity;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

public abstract class AbstractEntity implements Serializable {

    private static final long serialVersionUID = -8826342395112178831L;

    @Override
    public String toString() {
        try {
            Map<?, ?> map = PropertyUtils.describe(this);
            return map.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "null";
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode(){
        return super.hashCode();
    }
    
    /**
     * 不保存的域
     * @return
     */
    public String[] escap(){
        String[] escap = {"class"};
        return escap;
    }

}
