package com.pandawork.core.search.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * key-value 形式的注解参数
 * 
 * @author Administrator
 * 
 */
@Target({})
@Retention(RUNTIME)
public @interface Parameter {
	String name();

	String value();
}
