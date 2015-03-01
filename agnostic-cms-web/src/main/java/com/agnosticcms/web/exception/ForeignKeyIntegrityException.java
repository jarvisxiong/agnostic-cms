package com.agnosticcms.web.exception;

public class ForeignKeyIntegrityException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ForeignKeyIntegrityException(String message, Throwable cause) {
		super(message, cause);
	}

	public ForeignKeyIntegrityException(String message) {
		super(message);
	}

	public ForeignKeyIntegrityException(Throwable cause) {
		super(cause);
	}
	
}
