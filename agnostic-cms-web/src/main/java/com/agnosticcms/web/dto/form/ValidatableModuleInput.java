package com.agnosticcms.web.dto.form;

import java.util.List;

import com.agnosticcms.web.dto.ModuleColumn;

public class ValidatableModuleInput {

	private ModuleInput moduleInput;
	private List<ModuleColumn> moduleColumns;
	
	
	public ModuleInput getModuleInput() {
		return moduleInput;
	}
	public void setModuleInput(ModuleInput moduleInput) {
		this.moduleInput = moduleInput;
	}
	public List<ModuleColumn> getModuleColumns() {
		return moduleColumns;
	}
	public void setModuleColumns(List<ModuleColumn> moduleColumns) {
		this.moduleColumns = moduleColumns;
	}
	
}
