package com.pandawork.core.search.engine;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.NumericField;

import com.pandawork.core.search.annotations.StoreEum;
import com.pandawork.core.search.bridge.utils.NumericFieldUtils;
import com.pandawork.core.util.StringHelper;

public class LuceneOptions {
	private static final int PRECISION_STEP = 4;
	
	private final Index indexMode;
	private final StoreEum storeType;
	private final boolean storeCompressed;
	private final boolean storeUncompressed;
	
	public LuceneOptions(Index indexMode, StoreEum storeType) {
		this.indexMode = indexMode;
		this.storeType = storeType;
		// TODO 没有实现存储的压缩
		this.storeCompressed = false;
		this.storeUncompressed = storeType.equals(StoreEum.YES);
	}
	
	public void addFieldToDocument(String fieldName, String indexedString,
			Document document) {
		if (!StringHelper.isEmpty(indexedString)) {
			if (!(indexMode.equals(Field.Index.NO))) {
				standardFieldAdd(fieldName,indexedString,document);
			}
		}
	}
	
	public void addNumericFieldToDocument(String fieldName,
			Object numericValue, Document document) {
		if(numericValue != null){
			NumericField field = new NumericField(fieldName, PRECISION_STEP, storeType != StoreEum.NO ? Field.Store.YES : Field.Store.NO, true);
			NumericFieldUtils.setNumericValue(numericValue, field);
			
			if(field.getNumericValue() != null){
				document.add(field);
			}
		}
	}
	
	private void standardFieldAdd(String name, String indexedString,
			Document document) {
		Field field = new Field(name, indexedString, storeUncompressed ?Field.Store.YES :Field.Store.NO, indexMode);

		document.add(field);
	}
}
