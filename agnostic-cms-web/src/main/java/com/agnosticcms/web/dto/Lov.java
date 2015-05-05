package com.agnosticcms.web.dto;

import java.util.List;

/**
 * List of values used for showing human-readable values to the user when it chooses or views
 * parents and childs of module elements
 */
public class Lov {

	/**
	 * Type of the list of values column
	 */
	private ColumnType type;
	
	/**
	 * Items for the current list of values
	 */
	private List<LovItem> items;
	
	
	public ColumnType getType() {
		return type;
	}
	public void setType(ColumnType type) {
		this.type = type;
	}
	public List<LovItem> getItems() {
		return items;
	}
	public void setItems(List<LovItem> items) {
		this.items = items;
	}
	
}
