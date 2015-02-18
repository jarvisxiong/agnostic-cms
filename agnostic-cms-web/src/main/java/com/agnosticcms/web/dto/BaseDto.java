package com.agnosticcms.web.dto;

public class BaseDto {

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
