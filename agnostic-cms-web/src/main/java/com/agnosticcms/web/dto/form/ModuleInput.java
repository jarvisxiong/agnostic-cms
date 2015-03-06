package com.agnosticcms.web.dto.form;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public class ModuleInput {

	private Map<Integer, Long> lovValues;
	private Map<Long, String> columnValues;
	private Map<Long, MultipartFile> files;
	
	
	public ModuleInput() {
		lovValues = new HashMap<>();
		columnValues = new HashMap<>();
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
	public Map<Long, MultipartFile> getFiles() {
		return files;
	}
	public void setFiles(Map<Long, MultipartFile> files) {
		this.files = files;
	}
	
}
