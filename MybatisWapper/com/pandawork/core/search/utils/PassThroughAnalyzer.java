package com.pandawork.core.search.utils;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharTokenizer;
import org.apache.lucene.analysis.TokenStream;

public final class PassThroughAnalyzer extends Analyzer {
	public TokenStream tokenStream(String fieldName, Reader reader) {
		return new PassThroughTokenizer(reader);
	}

	private static class PassThroughTokenizer extends CharTokenizer {
		public PassThroughTokenizer(Reader input) {
			super(input);
		}

		@Override
		protected boolean isTokenChar(char c) {
			return true;
		}
	}
}
