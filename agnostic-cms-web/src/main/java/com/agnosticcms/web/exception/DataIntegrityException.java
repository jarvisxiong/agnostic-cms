package com.agnosticcms.web.exception;

public class DataIntegrityException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String i18nMessageCode;

	public DataIntegrityException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataIntegrityException(String message) {
		super(message);
	}
	
	
	public DataIntegrityException(Throwable cause, String i18nMessageCode) {
		super(cause);
		this.i18nMessageCode = i18nMessageCode;
	}

	public DataIntegrityException(String message, String i18nMessageCode) {
		super(message);
		this.i18nMessageCode = i18nMessageCode;
	}
	

	public String getI18nMessageCode() {
		return i18nMessageCode;
	}
	
}
