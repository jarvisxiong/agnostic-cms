package com.agnosticcms.web.dto;

public class ClassifierItem extends BaseDto {

	private Object value;

	public ClassifierItem() {
	}

	public ClassifierItem(Long id, Object value) {
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
