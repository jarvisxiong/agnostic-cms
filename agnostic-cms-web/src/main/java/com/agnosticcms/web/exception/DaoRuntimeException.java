package com.agnosticcms.web.exception;

/**
 * Runtime exception that is thrown when there is a failure in data access functionality execution
 */
public class DaoRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DaoRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public DaoRuntimeException(String message) {
		super(message);
	}

	public DaoRuntimeException(Throwable cause) {
		super(cause);
	}
	
}
