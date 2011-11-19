package com.pandawork.core.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体注解表示注释 
 * 使用该注释注解后，会自动根据配置的包名进行扫描，并注册到hibernate容器中
 * 
 * @author Lionel pang
 * @date 2010-3-21
 */
@Target(value={ElementType.TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface EntityAnnotation  {

}
