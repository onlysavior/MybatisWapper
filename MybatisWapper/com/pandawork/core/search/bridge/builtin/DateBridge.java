package com.pandawork.core.search.bridge.builtin;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.lucene.document.DateTools;

import com.pandawork.core.search.annotations.Resolution;
import com.pandawork.core.search.bridge.ParameterizedBridge;
import com.pandawork.core.search.bridge.TwoWayStringBridge;
import com.pandawork.core.util.StringHelper;

public class DateBridge implements TwoWayStringBridge, ParameterizedBridge {

	public static final TwoWayStringBridge DATE_YEAR = new DateBridge(
			Resolution.YEAR);
	public static final TwoWayStringBridge DATE_MONTH = new DateBridge(
			Resolution.MONTH);
	public static final TwoWayStringBridge DATE_DAY = new DateBridge(
			Resolution.DAY);
	public static final TwoWayStringBridge DATE_HOUR = new DateBridge(
			Resolution.HOUR);
	public static final TwoWayStringBridge DATE_MINUTE = new DateBridge(
			Resolution.MINUTE);
	public static final TwoWayStringBridge DATE_SECOND = new DateBridge(
			Resolution.SECOND);
	public static final TwoWayStringBridge DATE_MILLISECOND = new DateBridge(
			Resolution.MILLISECOND);

	private DateTools.Resolution resolution;

	public DateBridge() {
	}

	public DateBridge(Resolution resolution) {
		this.resolution = DateResolutionUtil.getLuceneResolution(resolution);
	}

	public String objectToString(Object object) {
		return object != null ? DateTools.dateToString((Date) object,
				resolution) : null;
	}

	public void setParameterValues(Map<?,?> parameters) {
		Object resolution = parameters.get( "resolution" );
		Resolution hibResolution;
		if ( resolution instanceof String ) {
			hibResolution = Resolution.valueOf( ( (String) resolution ).toUpperCase( Locale.ENGLISH ) );
		}
		else {
			hibResolution = (Resolution) resolution;
		}
		this.resolution = DateResolutionUtil.getLuceneResolution( hibResolution );
	}

	public Object stringToObject(String stringValue) {
		if (StringHelper.isEmpty(stringValue))
			return null;
		try {
			return DateTools.stringToDate(stringValue);
		} catch (ParseException e) {
			throw new RuntimeException("Unable to parse into date: "
					+ stringValue, e);
		}
	}

}
