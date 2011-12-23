package com.pandawork.core.search.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 定义DB Index
 * 
 * @author Administrator
 * 
 */
@Target({ ElementType.METHOD, ElementType.FIELD,ElementType.TYPE })
@Retention(RUNTIME)
public @interface Index {
	String name() default "";

	String[] columnNames() default {};
	
	String index() default "";
}
