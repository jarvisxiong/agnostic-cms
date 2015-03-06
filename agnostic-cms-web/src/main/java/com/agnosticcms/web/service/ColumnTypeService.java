package com.agnosticcms.web.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.agnosticcms.web.dto.ColumnType;
import com.agnosticcms.web.exception.TypeConversionException;

@Service
public class ColumnTypeService {

	public Object parseFromString(String value, ColumnType columnType) throws TypeConversionException {
		
		if(StringUtils.isEmpty(value) && columnType != ColumnType.BOOL) {
			return null;
		}
		
		switch (columnType) {
		case BOOL:
			return parseBoolean(value);
		case INT:
			return parseInt(value);
		case LONG:
			return parseLong(value);
		case STRING:
		case ENUM:
			return value;
		default:
			throw new IllegalArgumentException("Unsupported column type " + columnType);
		}
		
	}
	
	private Boolean parseBoolean(String value) {
		return "true".equals(value);
	}
	
	private Integer parseInt(String value) throws TypeConversionException {
		try {
			return Integer.valueOf(value);
		} catch(NumberFormatException e) {
			throw new TypeConversionException("Failed to convert string to Integer", e);
		}
		
	}
	
	private Long parseLong(String value) throws TypeConversionException {
		try {
			return Long.valueOf(value);
		} catch(NumberFormatException e) {
			throw new TypeConversionException("Failed to convert string to Long", e);
		}
	}
	
	public String parseToString(Object value, ColumnType columnType) {
		
		if(value == null) {
			return null;
		}
		
		switch (columnType) {
		case BOOL:
			return ((Boolean) value) ? "true" : "false";
		case INT:
		case LONG:
			return value.toString();
		case STRING:
		case ENUM:
			return (String) value;
		default:
			throw new IllegalArgumentException("Unsupported column type " + columnType);
		}
		
	}
}
