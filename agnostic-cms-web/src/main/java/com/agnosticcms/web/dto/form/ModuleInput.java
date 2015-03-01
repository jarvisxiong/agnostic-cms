package com.agnosticcms.web.dto.form;

import java.util.Map;

public class ModuleInput {

	private Map<Integer, Long> lovValues;
	private Map<Long, String> columnValues;
	
	
	public ModuleInput() {
	}

	public ModuleInput(Map<Integer, Long> lovValues, Map<Long, String> columnValues) {
		this.lovValues = lovValues;
		this.columnValues = columnValues;
	}
	
	public Map<Integer, Long> getLovValues() {
		return lovValues;
	}
	public void setLovValues(Map<Integer, Long> lovValues) {
		this.lovValues = lovValues;
	}
	public Map<Long, String> getColumnValues() {
		return columnValues;
	}
	public void setColumnValues(Map<Long, String> columnValues) {
		this.columnValues = columnValues;
	}
	
}
