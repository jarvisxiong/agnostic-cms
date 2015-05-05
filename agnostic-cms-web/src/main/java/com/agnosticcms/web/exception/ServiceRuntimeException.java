package com.agnosticcms.web.exception;

/**
 * Runtime exception that is thrown when there is a failure in service execution process
 */
public class ServiceRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ServiceRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceRuntimeException(String message) {
		super(message);
	}

	public ServiceRuntimeException(Throwable cause) {
		super(cause);
	}
	
}
