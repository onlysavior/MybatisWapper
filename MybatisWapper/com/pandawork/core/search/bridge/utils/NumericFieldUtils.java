package com.pandawork.core.search.bridge.utils;

import org.apache.lucene.document.NumericField;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;


public final class NumericFieldUtils {
	@SuppressWarnings("unchecked")
	public static Query createNumericRangeQuery(String fieldName, Object from,
			Object to, boolean includeLower, boolean includeUpper) {

		Class numericClass;

		if (from != null) {
			numericClass = from.getClass();
		} else if (to != null) {
			numericClass = to.getClass();
		} else {
			throw new RuntimeException(
					"Cannot create numeric range query for field " + fieldName
							+ ", since from and to values are " + "null");
		}

		if (numericClass.isAssignableFrom(Double.class)) {
			return NumericRangeQuery.newDoubleRange(fieldName, (Double) from,
					(Double) to, includeLower, includeUpper);
		}
		if (numericClass.isAssignableFrom(Long.class)) {
			return NumericRangeQuery.newLongRange(fieldName, (Long) from,
					(Long) to, includeLower, includeUpper);
		}
		if (numericClass.isAssignableFrom(Integer.class)) {
			return NumericRangeQuery.newIntRange(fieldName, (Integer) from,
					(Integer) to, includeLower, includeUpper);
		}
		if (numericClass.isAssignableFrom(Float.class)) {
			return NumericRangeQuery.newFloatRange(fieldName, (Float) from,
					(Float) to, includeLower, includeUpper);
		}
		throw new RuntimeException(
				"Cannot create numeric range query for field " + fieldName
						+ ", since values are not numeric "
						+ "(int,long, short or double) ");
	}
	
	public static Query createExactMatchQuery(String fieldName, Object value) {
		return createNumericRangeQuery(fieldName, value, value, true, true);
	}
	
	@SuppressWarnings("unchecked")
	public static void setNumericValue(Object value, NumericField numericField) {
		Class numericClass = value.getClass();
		if ( numericClass.isAssignableFrom( Double.class ) ) {
			numericField.setDoubleValue( (Double) value );
		}
		if ( numericClass.isAssignableFrom( Long.class ) ) {
			numericField.setLongValue( (Long) value );
		}
		if ( numericClass.isAssignableFrom( Integer.class ) ) {
			numericField.setIntValue( (Integer) value );
		}
		if ( numericClass.isAssignableFrom( Float.class ) ) {
			numericField.setFloatValue( (Float) value );
		}
	}
}
