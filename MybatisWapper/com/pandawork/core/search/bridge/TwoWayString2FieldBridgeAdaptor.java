package com.pandawork.core.search.bridge;

import java.util.zip.DataFormatException;

import org.apache.lucene.document.CompressionTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class TwoWayString2FieldBridgeAdaptor extends String2FieldBridgeAdaptor
		implements TwoWayFieldBridge {

	private final TwoWayStringBridge stringBridge;

	public TwoWayString2FieldBridgeAdaptor(TwoWayStringBridge stringBridge) {
		super(stringBridge);
		this.stringBridge = stringBridge;
	}

	public String objectToString(Object object) {
		return stringBridge.objectToString(object);
	}

	public Object get(String name, Document document) {
		Field field = document.getField(name);
		if (field == null) {
			return stringBridge.stringToObject(null);
		} else {
			String stringValue;
			if (field.isBinary()) {
				try {
					stringValue = CompressionTools.decompressString(field
							.getBinaryValue());
				} catch (DataFormatException e) {
					throw new RuntimeException(e);
				}
			} else {
				stringValue = field.stringValue();
			}
			return stringBridge.stringToObject(stringValue);
		}
	}

	public TwoWayStringBridge unwrap() {
		return stringBridge;
	}
}
