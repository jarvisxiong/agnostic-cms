package com.agnosticcms.web.dto;

/**
 * Single list of values item
 */
public class LovItem extends BaseDto {

	/**
	 * The value of the list item
	 */
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
