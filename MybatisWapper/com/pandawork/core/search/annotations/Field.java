package com.pandawork.core.search.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Documented
public @interface Field {
	/** Field的名字，默认是属性名 */
	String name() default "";

	/** 是否选择存储 */
	StoreEum store() default StoreEum.NO;

	/** 选择是否分词 */
	IndexEum index() default IndexEum.TOKENIZED;

	/** 选择的分词器 */
	Analyzer analyzer() default @Analyzer;

	/** 选择的FieldBridge */
	FieldBridge bridge() default @FieldBridge;
}
