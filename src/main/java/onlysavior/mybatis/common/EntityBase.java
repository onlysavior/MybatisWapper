package onlysavior.mybatis.common;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 * the base class of all the entities
 */
public abstract class EntityBase implements Serializable {
    abstract long getId();
    abstract Date getGmtGreate();
    abstract Date getGmtModify();

    protected Collection<String> excludeField() {
        Collection<String> excludes = new LinkedList<String>();
        excludes.add("class");

        return excludes;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder
                .toStringExclude(this, excludeField());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof EntityBase))
            return false;
        EntityBase other = (EntityBase)obj;
        return getId() == other.getId();
    }
}
