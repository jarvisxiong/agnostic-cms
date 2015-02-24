package com.agnosticcms.web.dto;

import java.util.List;

public class Lov {

	private ColumnType type;
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
