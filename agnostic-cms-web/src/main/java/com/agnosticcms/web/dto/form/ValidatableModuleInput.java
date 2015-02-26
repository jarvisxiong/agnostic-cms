package com.agnosticcms.web.dto.form;

import java.util.List;

import com.agnosticcms.web.dto.ModuleColumn;
import com.agnosticcms.web.dto.ModuleHierarchy;

public class ValidatableModuleInput {

	private ModuleInput moduleInput;
	private List<ModuleColumn> moduleColumns;
	private List<ModuleHierarchy> moduleHierarchies;
	
	
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
	public List<ModuleHierarchy> getModuleHierarchies() {
		return moduleHierarchies;
	}
	public void setModuleHierarchies(List<ModuleHierarchy> moduleHierarchies) {
		this.moduleHierarchies = moduleHierarchies;
	}
	
}
