package com.pandawork.core.search.bridge.builtin;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;

public class DateResolutionUtil {
	private DateResolutionUtil() {}
	
	
	public static Resolution getLuceneResolution(com.pandawork.core.search.annotations.Resolution ibResolution) {
		Resolution resolution = null;
		switch (ibResolution) {
			case YEAR:
				resolution = DateTools.Resolution.YEAR;
				break;
			case MONTH:
				resolution = DateTools.Resolution.MONTH;
				break;
			case DAY:
				resolution = DateTools.Resolution.DAY;
				break;
			case HOUR:
				resolution = DateTools.Resolution.HOUR;
				break;
			case MINUTE:
				resolution = DateTools.Resolution.MINUTE;
				break;
			case SECOND:
				resolution = DateTools.Resolution.SECOND;
				break;
			case MILLISECOND:
				resolution = DateTools.Resolution.MILLISECOND;
				break;
			default:
				throw new RuntimeException( "没这个东西: " + ibResolution );
		}
		return resolution;
	}
}
