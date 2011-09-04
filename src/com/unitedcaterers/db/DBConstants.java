package com.unitedcaterers.db;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Class which contains all the constants related to the database file format
 * provided.
 * 
 * @author Seb
 * 
 */
public final class DBConstants {
	
	/**
	 * Private constructor as this class contains only constants.
	 */
	private DBConstants() {
		
	}
	
	public static final SimpleDateFormat	DATEFORMAT						= new SimpleDateFormat("yyyy/MM/dd",
																					Locale.US);
	
	/**
	 * Flag indicating smoking is allowed.
	 */
	public static final String				SMOKING_FLAG_YES				= "Y";
	
	/**
	 * Flag indicating smoking is not allowed.
	 */
	public static final String				SMOKING_FLAG_NO					= "N";
	
	/**
	 * Flag indicating an invalid record.
	 */
	public static final byte				INVALID_RECORD_FLAG				= -1;				// -1
																								// is
																								// the
																								// byte
																								// representation
																								// of
																								// 0xFF
																								
	/**
	 * Flag indicating a valid record.
	 */
	public static final byte				VALID_RECORD_FLAG				= 0;
	
	/**
	 * Correct value for the magic cookie.
	 */
	public static final int					MAGIC_COOKIE_REFERENCE			= 257;
	
	/**
	 * Length of the validity flag.
	 */
	public static final int					VALIDITY_FLAG_LENGTH			= 1;
	
	/**
	 * Length of the hotel name field.
	 */
	public static final int					HOTEL_NAME_LENGTH				= 64;
	
	/**
	 * Length of the city name field.
	 */
	public static final int					CITY_LENGTH						= 64;
	
	/**
	 * Length of the room size field.
	 */
	public static final int					SIZE_LENGTH						= 4;
	
	/**
	 * Length of the smoking flag.
	 */
	public static final int					SMOKING_LENGTH					= 1;
	
	/**
	 * Length of the rate field.
	 */
	public static final int					RATE_LENGTH						= 8;
	
	/**
	 * Length of the date field.
	 */
	public static final int					DATE_LENGTH						= 10;
	
	/**
	 * Length of the customer ID field.
	 */
	public static final int					CUSTOMER_ID_LENGTH				= 8;
	
	/**
	 * Global length of a record.
	 */
	public static final int					RECORD_LENGTH					= 159;
	
	/**
	 * Length of a name field.
	 */
	public static final int					FIELD_NAME_LENGTH				= 1;
	
	/**
	 * Length of the field which contains the length indicator of a field.
	 */
	public static final int					FIELD_LENGTH_INDICATOR_LENGTH	= 1;
	
	// Order of the different fields in an array of String
	// This is the order provided by the database file
	
	/**
	 * Position of the validity flag when representing a Room as an array of
	 * String.
	 */
	public static final int					VALID_POSITION					= 0;
	
	/**
	 * Position of the hotel name when representing a Room as an array of
	 * String.
	 */
	public static final int					HOTEL_NAME_POSITION				= 1;
	
	/**
	 * Position of a city name when representing a Room as an array of String.
	 */
	public static final int					CITY_POSITION					= 2;
	
	/**
	 * Position of the room size when representing a Room as an array of String.
	 */
	public static final int					SIZE_POSITION					= 3;
	
	/**
	 * Position of the smoking flag when representing a Room as an array of
	 * String.
	 */
	public static final int					SMOKING_POSITION				= 4;
	
	/**
	 * Position of the rate when representing a Room as an array of String.
	 */
	public static final int					RATE_POSITION					= 5;
	
	/**
	 * Position of the date when representing a Room as an array of String.
	 */
	public static final int					DATE_POSITION					= 6;
	
	/**
	 * Position of the validity flag when representing a Room as an array of
	 * String.
	 */
	public static final int					CUSTOMER_ID_POSITION			= 7;
	
	/**
	 * Number of fields in a record.
	 */
	public static final int					NUMBER_OF_FIELDS				= 8;
	
	/**
	 * Length of the cookie.
	 */
	public static final int					COOKIE_SIZE						= 4;
	
	/**
	 * Length of the field which indicates the number of fields in a record.
	 */
	public static final int					NUMBER_OF_FIELDS_SIZE			= 2;
	
}
