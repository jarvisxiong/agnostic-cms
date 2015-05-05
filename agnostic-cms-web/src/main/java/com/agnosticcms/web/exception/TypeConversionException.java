package com.agnosticcms.web.exception;

/**
 * Exception that is thrown when there is a failure during conversion to or from module column types
 */
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
