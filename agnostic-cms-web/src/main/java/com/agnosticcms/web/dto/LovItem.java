package com.agnosticcms.web.dto;

public class LovItem extends BaseDto {

	private Object value;

	public LovItem() {
	}

	public LovItem(Long id, Object value) {
		super(id);
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
}
