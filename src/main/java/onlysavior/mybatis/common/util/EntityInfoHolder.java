package onlysavior.mybatis.common.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * the entity info holder
 * throw exeception when meet depuliate key
 */
public class EntityInfoHolder extends ConcurrentHashMap<Class, String> {
    private final String name;

    public EntityInfoHolder(String n) {
        this.name = n;
    }

    public void addClass(Class clz, String name) {
        if(containsKey(clz)) {
           throw new IllegalStateException("class depuliate :"+clz);
        }
        put(clz, name);
    }

    @Override
    public String put(Class key, String value) {
        if (containsKey(key)) {
           throw new IllegalStateException("class depuliate :"+key);
        }
        return super.put(key, value);
    }

    public String getName(Class clz) {
        return name;
    }
}
