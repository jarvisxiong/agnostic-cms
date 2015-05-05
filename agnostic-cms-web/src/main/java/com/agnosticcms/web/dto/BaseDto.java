package com.agnosticcms.web.dto;

/**
 * Parent class for standard Agnostic CMS database data transfer objects
 */
public abstract class BaseDto {

	/**
	 * Unique identifier of the object in it's kind
	 */
	private Long id;

	public BaseDto() {
	}

	public BaseDto(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
