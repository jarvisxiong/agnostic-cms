package com.agnosticcms.web.exception;

public class TypeConversionException extends Exception {

	private static final long serialVersionUID = 1L;

	public TypeConversionException() {
		super();
	}

	public TypeConversionException(String message, Throwable cause) {
		super(message, cause);
	}

	public TypeConversionException(String message) {
		super(message);
	}

	public TypeConversionException(Throwable cause) {
		super(cause);
	}
	
}
