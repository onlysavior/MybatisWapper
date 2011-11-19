package com.pandawork.core.search.bridge.builtin;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.lucene.document.DateTools;

import com.pandawork.core.search.annotations.Resolution;
import com.pandawork.core.search.bridge.ParameterizedBridge;
import com.pandawork.core.search.bridge.TwoWayStringBridge;
import com.pandawork.core.util.StringHelper;

public class CalendarBridge implements TwoWayStringBridge, ParameterizedBridge {
	
	public static final String RESOLUTION_PARAMETER = "resolution";
	
	private DateTools.Resolution resolution;
	public static final TwoWayStringBridge CALENDAR_YEAR = new CalendarBridge( Resolution.YEAR );
	public static final TwoWayStringBridge CALENDAR_MONTH = new CalendarBridge( Resolution.MONTH );
	public static final TwoWayStringBridge CALENDAR_DAY = new CalendarBridge( Resolution.DAY );
	public static final TwoWayStringBridge CALENDAR_HOUR = new CalendarBridge( Resolution.HOUR );
	public static final TwoWayStringBridge CALENDAR_MINUTE = new CalendarBridge( Resolution.MINUTE );
	public static final TwoWayStringBridge CALENDAR_SECOND = new CalendarBridge( Resolution.SECOND );
	public static final TwoWayStringBridge CALENDAR_MILLISECOND = new CalendarBridge( Resolution.MILLISECOND );
	
	public CalendarBridge(){}
	
	public CalendarBridge(Resolution resolution) {
		this.resolution = DateResolutionUtil.getLuceneResolution( resolution);
	}

	public String objectToString(Object object) {
        if (object == null) {
            return null;
        }
        Calendar calendar = (Calendar)object;
        return DateTools.dateToString(calendar.getTime(),resolution);
    }

	public void setParameterValues(Map<?,?> parameters) {
		Object resolution = parameters.get( RESOLUTION_PARAMETER );
		Resolution ibResolution;
		if ( resolution instanceof String ) {
			ibResolution = Resolution.valueOf( ( (String) resolution ).toUpperCase( Locale.ENGLISH ) );
		}
		else {
			ibResolution = (Resolution) resolution;
		}
		this.resolution = DateResolutionUtil.getLuceneResolution( ibResolution );
	}

    public Object stringToObject(String stringValue) {
		if ( StringHelper.isEmpty( stringValue ) ) {
            return null;
        }
        try {
            Date date = DateTools.stringToDate( stringValue );
            Calendar calendar = Calendar.getInstance();
            calendar.setTime( date );
            return calendar;
        } catch (ParseException e) {
            throw new RuntimeException( "Unable to parse into calendar: " + stringValue, e );
        }
	}

}
