package com.agnosticcms.web.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.agnosticcms.web.dto.ColumnType;
import com.agnosticcms.web.exception.TypeConversionException;

/**
 * Service for parsing string values to Java types and vice-versa, determinated by column types
 */
@Service
public class ColumnTypeService {

	/**
	 * Parses a string value to appropriate Java type based on column type
	 * @param The string value
	 * @param columnType The column type to base the conversion upon
	 * @return Parsed object
	 * @throws TypeConversionException If there was an error during conversion
	 */
	public Object parseFromString(String value, ColumnType columnType) throws TypeConversionException {
		
		// Only boolean values are non-null parsed if passed string is empty
		if(StringUtils.isEmpty(value) && columnType != ColumnType.BOOL) {
			return null;
		}
		
		// Parse string based on column type
		switch (columnType) {
		case BOOL:
			return parseBoolean(value);
		case INT:
			return parseInt(value);
		case LONG:
			return parseLong(value);
		case DECIMAL:
			return parseDecimal(value);
		case STRING:
		case ENUM:
		case IMAGE:
		case HTML:
			return value;
		default:
			throw new IllegalArgumentException("Unsupported column type " + columnType);
		}
		
	}

	/**
	 * Parses string to Boolean
	 * @param value The string value
	 * @return The boolean value
	 */
	private Boolean parseBoolean(String value) {
		return "true".equals(value);
	}
	
	/**
	 * Parses string to integer
	 * @param value The string value
	 * @return The integer value
	 * @throws TypeConversionException If string is not a valid integer
	 */
	private Integer parseInt(String value) throws TypeConversionException {
		try {
			return Integer.valueOf(value);
		} catch(NumberFormatException e) {
			throw new TypeConversionException("Failed to convert string to Integer", e);
		}
	}
	
	/**
	 * Parses string to double
	 * @param value The string value
	 * @return The double value
	 * @throws TypeConversionException If string is not a valid double
	 */
	private Double parseDecimal(String value) throws TypeConversionException {
		value = value.replace(",", ".");
		
		try {
			return Double.valueOf(value);
		} catch(NumberFormatException e) {
			throw new TypeConversionException("Failed to convert string to Double", e);
		}
	}
	
	/**
	 * Parses string to long
	 * @param value The string value
	 * @return The long value
	 * @throws TypeConversionException If string is not a valid long
	 */
	private Long parseLong(String value) throws TypeConversionException {
		try {
			return Long.valueOf(value);
		} catch(NumberFormatException e) {
			throw new TypeConversionException("Failed to convert string to Long", e);
		}
	}
	
	/**
	 * Parses Java objects to string based on column type
	 * @param value The java object value
	 * @param columnType The column type to base conversion upon
	 * @return Parsed string value
	 */
	public String parseToString(Object value, ColumnType columnType) {
		
		if(value == null) {
			return null;
		}
		
		switch (columnType) {
		case BOOL:
			return ((Boolean) value) ? "true" : "false";
		case INT:
		case LONG:
		case DECIMAL:
			return value.toString();
		case STRING:
		case ENUM:
		case IMAGE:
		case HTML:
			return (String) value;
		default:
			throw new IllegalArgumentException("Unsupported column type " + columnType);
		}
		
	}
}
