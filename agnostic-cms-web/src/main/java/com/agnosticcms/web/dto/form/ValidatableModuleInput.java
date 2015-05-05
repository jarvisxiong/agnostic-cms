package com.agnosticcms.web.dto.form;

import java.util.List;

import com.agnosticcms.web.dto.ModuleColumn;
import com.agnosticcms.web.dto.ModuleHierarchy;


/**
 * Contains module element input and additional metadata needed for it's validation
 */
public class ValidatableModuleInput {

	/**
	 * Module element input from form submission
	 */
	private ModuleInput moduleInput;
	
	/**
	 * Columns of a module whose element is posted
	 */
	private List<ModuleColumn> moduleColumns;
	
	/**
	 * Hierarchy elements of a module (those where the module is involved as a child)
	 * whose element is posted
	 */
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
