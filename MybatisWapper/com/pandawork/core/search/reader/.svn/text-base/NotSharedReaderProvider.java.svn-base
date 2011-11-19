package com.pandawork.core.search.reader;


import java.io.IOException;

import org.apache.lucene.index.IndexReader;

import com.pandawork.core.search.store.DirectoryProvider;

public class NotSharedReaderProvider {
	
	public IndexReader openReader(DirectoryProvider<?> directoryProviders) {
		IndexReader reader = null;
		try {
			 reader = IndexReader.open(directoryProviders.getDirectory(), true);
		} catch(Exception e){
			throw new RuntimeException("不能打开 indexs");
		}
		
		return reader;
	}


	public void closeReader(IndexReader reader) {
		try {
			reader.close();
		}
		catch (IOException e) {
		}
	}


	public void destroy() {
	}
}
