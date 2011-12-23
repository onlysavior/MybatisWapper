package com.pandawork.core.search.bridge;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.pandawork.core.search.annotations.Field;
import com.pandawork.core.search.annotations.NumericField;
import com.pandawork.core.search.annotations.Resolution;
import com.pandawork.core.search.bridge.builtin.BigDecimalBridge;
import com.pandawork.core.search.bridge.builtin.BigIntegerBridge;
import com.pandawork.core.search.bridge.builtin.BooleanBridge;
import com.pandawork.core.search.bridge.builtin.CalendarBridge;
import com.pandawork.core.search.bridge.builtin.CharacterBridge;
import com.pandawork.core.search.bridge.builtin.ClassBridge;
import com.pandawork.core.search.bridge.builtin.DateBridge;
import com.pandawork.core.search.bridge.builtin.DoubleBridge;
import com.pandawork.core.search.bridge.builtin.DoubleNumericFieldBridge;
import com.pandawork.core.search.bridge.builtin.EnumBridge;
import com.pandawork.core.search.bridge.builtin.FloatBridge;
import com.pandawork.core.search.bridge.builtin.FloatNumericFieldBridge;
import com.pandawork.core.search.bridge.builtin.IntegerBridge;
import com.pandawork.core.search.bridge.builtin.IntegerNumericFieldBridge;
import com.pandawork.core.search.bridge.builtin.LongBridge;
import com.pandawork.core.search.bridge.builtin.LongNumericFieldBridge;
import com.pandawork.core.search.bridge.builtin.NumericFieldBridge;
import com.pandawork.core.search.bridge.builtin.ShortBridge;
import com.pandawork.core.search.bridge.builtin.UriBridge;
import com.pandawork.core.search.bridge.builtin.UrlBridge;



public final class BridgeFactory {
	private static Map<String, FieldBridge> builtInBridges = new HashMap<String, FieldBridge>();
	private static Map<String, NumericFieldBridge> numericBridges = new HashMap<String, NumericFieldBridge>();

	private BridgeFactory() {
	}

	public static final TwoWayFieldBridge CHARACTER = new TwoWayString2FieldBridgeAdaptor( new CharacterBridge() );
	public static final TwoWayFieldBridge DOUBLE = new TwoWayString2FieldBridgeAdaptor( new DoubleBridge() );
	public static final TwoWayFieldBridge FLOAT = new TwoWayString2FieldBridgeAdaptor( new FloatBridge() );
	public static final TwoWayFieldBridge SHORT = new TwoWayString2FieldBridgeAdaptor( new ShortBridge() );
	public static final TwoWayFieldBridge INTEGER = new TwoWayString2FieldBridgeAdaptor( new IntegerBridge() );
	public static final TwoWayFieldBridge LONG = new TwoWayString2FieldBridgeAdaptor( new LongBridge() );
	public static final TwoWayFieldBridge BIG_INTEGER = new TwoWayString2FieldBridgeAdaptor( new BigIntegerBridge() );
	public static final TwoWayFieldBridge BIG_DECIMAL = new TwoWayString2FieldBridgeAdaptor( new BigDecimalBridge() );
	public static final TwoWayFieldBridge BOOLEAN = new TwoWayString2FieldBridgeAdaptor( new BooleanBridge() );
	public static final TwoWayFieldBridge CLAZZ = new TwoWayString2FieldBridgeAdaptor( new ClassBridge() );
	public static final TwoWayFieldBridge Url = new TwoWayString2FieldBridgeAdaptor( new UrlBridge() );
	public static final TwoWayFieldBridge Uri = new TwoWayString2FieldBridgeAdaptor( new UriBridge() );

	public static final FieldBridge DATE_YEAR = new TwoWayString2FieldBridgeAdaptor( DateBridge.DATE_YEAR );
	public static final FieldBridge DATE_MONTH = new TwoWayString2FieldBridgeAdaptor( DateBridge.DATE_MONTH );
	public static final FieldBridge DATE_DAY = new TwoWayString2FieldBridgeAdaptor( DateBridge.DATE_DAY );
	public static final FieldBridge DATE_HOUR = new TwoWayString2FieldBridgeAdaptor( DateBridge.DATE_HOUR );
	public static final FieldBridge DATE_MINUTE = new TwoWayString2FieldBridgeAdaptor( DateBridge.DATE_MINUTE );
	public static final FieldBridge DATE_SECOND = new TwoWayString2FieldBridgeAdaptor( DateBridge.DATE_SECOND );

	public static final FieldBridge CALENDAR_YEAR = new TwoWayString2FieldBridgeAdaptor( CalendarBridge.CALENDAR_YEAR );
	public static final FieldBridge CALENDAR_MONTH = new TwoWayString2FieldBridgeAdaptor( CalendarBridge.CALENDAR_MONTH );
	public static final FieldBridge CALENDAR_DAY = new TwoWayString2FieldBridgeAdaptor( CalendarBridge.CALENDAR_DAY );
	public static final FieldBridge CALENDAR_HOUR = new TwoWayString2FieldBridgeAdaptor( CalendarBridge.CALENDAR_HOUR );
	public static final FieldBridge CALENDAR_MINUTE = new TwoWayString2FieldBridgeAdaptor( CalendarBridge.CALENDAR_MINUTE );
	public static final FieldBridge CALENDAR_SECOND = new TwoWayString2FieldBridgeAdaptor( CalendarBridge.CALENDAR_SECOND );

	public static final NumericFieldBridge INT_NUMERIC = new IntegerNumericFieldBridge();
	public static final NumericFieldBridge LONG_NUMERIC = new LongNumericFieldBridge();
	public static final NumericFieldBridge FLOAT_NUMERIC = new FloatNumericFieldBridge();
	public static final NumericFieldBridge DOUBLE_NUMERIC = new DoubleNumericFieldBridge();
	
	public static final TwoWayFieldBridge DATE_MILLISECOND =
			new TwoWayString2FieldBridgeAdaptor( DateBridge.DATE_MILLISECOND );

	public static final TwoWayFieldBridge CALENDAR_MILLISECOND =
			new TwoWayString2FieldBridgeAdaptor( CalendarBridge.CALENDAR_MILLISECOND );
	
	static {
		builtInBridges.put( Character.class.getName(), CHARACTER );
		builtInBridges.put( char.class.getName(), CHARACTER );
		builtInBridges.put( Double.class.getName(), DOUBLE );
		builtInBridges.put( double.class.getName(), DOUBLE );
		builtInBridges.put( Float.class.getName(), FLOAT );
		builtInBridges.put( float.class.getName(), FLOAT );
		builtInBridges.put( Short.class.getName(), SHORT );
		builtInBridges.put( short.class.getName(), SHORT );
		builtInBridges.put( Integer.class.getName(), INTEGER );
		builtInBridges.put( int.class.getName(), INTEGER );
		builtInBridges.put( Long.class.getName(), LONG );
		builtInBridges.put( long.class.getName(), LONG );
		builtInBridges.put( BigInteger.class.getName(), BIG_INTEGER );
		builtInBridges.put( BigDecimal.class.getName(), BIG_DECIMAL );
		builtInBridges.put( Boolean.class.getName(), BOOLEAN );
		builtInBridges.put( boolean.class.getName(), BOOLEAN );
		builtInBridges.put( Class.class.getName(), CLAZZ );
		builtInBridges.put( URL.class.getName(), Url );
		builtInBridges.put( URI.class.getName(), Uri );

		builtInBridges.put( Date.class.getName(), DATE_MILLISECOND );
		builtInBridges.put( Calendar.class.getName(), CALENDAR_MILLISECOND );

		numericBridges.put( Integer.class.getName(), INT_NUMERIC );
		numericBridges.put( int.class.getName(), INT_NUMERIC );
		numericBridges.put( Long.class.getName(), LONG_NUMERIC );
		numericBridges.put( long.class.getName(), LONG_NUMERIC );
		numericBridges.put( Double.class.getName(), DOUBLE_NUMERIC );
		numericBridges.put( double.class.getName(), DOUBLE_NUMERIC );
		numericBridges.put( Float.class.getName(), FLOAT_NUMERIC );
		numericBridges.put( float.class.getName(), FLOAT_NUMERIC );
	}
	
	public static FieldBridge guessType(Field field, NumericField numericField, Member member) {
		FieldBridge bridge;
		com.pandawork.core.search.annotations.FieldBridge bridgeAnn;
		//@Field bridge has priority over @FieldBridge
		if ( field != null && void.class != field.bridge().impl() ) {
			bridgeAnn = field.bridge();
		}
		else {
			bridgeAnn = ((AnnotatedElement) member).getAnnotation( com.pandawork.core.search.annotations.FieldBridge.class );
		}
		if ( bridgeAnn != null ) {
			bridge = doExtractType( bridgeAnn, member );
		}
		else if ( ((AnnotatedElement) member).isAnnotationPresent(  com.pandawork.core.search.annotations.DateBridge.class ) ) {
			Resolution resolution = ((AnnotatedElement) member).getAnnotation( com.pandawork.core.search.annotations.DateBridge.class )
					.resolution();
			bridge = getDateField( resolution );
		}
		else if ( ((AnnotatedElement) member).isAnnotationPresent( com.pandawork.core.search.annotations.CalendarBridge.class ) ) {
			Resolution resolution = ((AnnotatedElement) member).getAnnotation( com.pandawork.core.search.annotations.CalendarBridge.class )
					.resolution();
			bridge = getCalendarField( resolution );
		}
		else if ( numericField != null ) {
			bridge = numericBridges.get( member.getClass().getName());
		}
		else {
			//find in built-ins
//			Class<?> returnType = member.getClass().getComponentType();
			Class<?> returnType = null;
			if(member instanceof java.lang.reflect.Field){
				returnType = ((java.lang.reflect.Field)member).getType();
			}else{
				returnType = ((Method)member).getReturnType();
			}
			bridge = builtInBridges.get( returnType.getName() );
			if ( bridge == null && returnType.isEnum() ) {
				//we return one enum type bridge instance per property as it is customized per ReturnType
				final EnumBridge enumBridge = new EnumBridge();
				populateReturnType( member.getClass(), EnumBridge.class, enumBridge );
				bridge = new TwoWayString2FieldBridgeAdaptor( enumBridge );
			}
		}
//		if ( bridge == null && retur) {
//			throw new RuntimeException( "Unable to guess FieldBridge for " + member.getName() );
//		}
		return bridge;
	}
	
	private static FieldBridge doExtractType(
			com.pandawork.core.search.annotations.FieldBridge bridgeAnn,
			Member member
			) {
		return doExtractType( bridgeAnn, member.getName(), member.getClass() );
	}
	
	private static FieldBridge doExtractType(
			com.pandawork.core.search.annotations.FieldBridge bridgeAnn,
			String appliedOnName,
			Class<?> appliedOnType) {
		assert bridgeAnn != null : "@FieldBridge instance cannot be null";
		FieldBridge bridge;
		Class<?> impl = bridgeAnn.impl();
		if ( impl == void.class ) {
			throw new RuntimeException( "@FieldBridge with no implementation class defined in: " + appliedOnName );
		}
		try {
			Object instance = impl.newInstance();
			if ( FieldBridge.class.isAssignableFrom( impl ) ) {
				bridge = (FieldBridge) instance;
			}
			else if ( TwoWayStringBridge.class.isAssignableFrom( impl ) ) {
				bridge = new TwoWayString2FieldBridgeAdaptor(
						(TwoWayStringBridge) instance
				);
			}
			else if ( StringBridge.class.isAssignableFrom( impl ) ) {
				bridge = new String2FieldBridgeAdaptor( (StringBridge) instance );
			}
			else {
				throw new RuntimeException(
						"@FieldBridge implementation implements none of the field bridge interfaces: "
								+ impl + " in " + appliedOnName
				);
			}
			if ( bridgeAnn.params().length > 0 && ParameterizedBridge.class.isAssignableFrom( impl ) ) {
				Map<String, String> params = new HashMap<String, String>( bridgeAnn.params().length );
				for ( com.pandawork.core.search.annotations.Parameter param : bridgeAnn.params() ) {
					params.put( param.name(), param.value() );
				}
				( (ParameterizedBridge) instance ).setParameterValues( params );
			}
			populateReturnType( appliedOnType, impl, instance );
		}
		catch ( Exception e ) {
			throw new RuntimeException( "Unable to instantiate FieldBridge for " + appliedOnName, e );
		}
		return bridge;
	}
	
	private static void populateReturnType(Class<?> appliedOnType, Class<?> bridgeType, Object bridgeInstance) {
		if ( EnumBridge.class.isAssignableFrom( bridgeType ) ) {
			( ( EnumBridge ) bridgeInstance ).setAppliedOnType( appliedOnType );
		}
	}
	
	public static FieldBridge getDateField(Resolution resolution) {
		switch ( resolution ) {
			case YEAR:
				return DATE_YEAR;
			case MONTH:
				return DATE_MONTH;
			case DAY:
				return DATE_DAY;
			case HOUR:
				return DATE_HOUR;
			case MINUTE:
				return DATE_MINUTE;
			case SECOND:
				return DATE_SECOND;
			case MILLISECOND:
				return DATE_MILLISECOND;
			default:
				throw new RuntimeException( "Unknown Resolution: " + resolution );
		}
	}


	public static FieldBridge getCalendarField(Resolution resolution) {
		switch ( resolution ) {
			case YEAR:
				return CALENDAR_YEAR;
			case MONTH:
				return CALENDAR_MONTH;
			case DAY:
				return CALENDAR_DAY;
			case HOUR:
				return CALENDAR_HOUR;
			case MINUTE:
				return CALENDAR_MINUTE;
			case SECOND:
				return CALENDAR_SECOND;
			case MILLISECOND:
				return CALENDAR_MILLISECOND;
			default:
				throw new RuntimeException( "Unknown Resolution: " + resolution );
		}
	}

}
