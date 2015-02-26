package com.agnosticcms.web.dto.form;

import java.util.Map;

public class ModuleInput {

	private Map<Long, Long> lovValues;
	private Map<Long, String> columnValues;
	
	
	public Map<Long, Long> getLovValues() {
		return lovValues;
	}
	public void setLovValues(Map<Long, Long> lovValues) {
		this.lovValues = lovValues;
	}
	public Map<Long, String> getColumnValues() {
		return columnValues;
	}
	public void setColumnValues(Map<Long, String> columnValues) {
		this.columnValues = columnValues;
	}
	
}
