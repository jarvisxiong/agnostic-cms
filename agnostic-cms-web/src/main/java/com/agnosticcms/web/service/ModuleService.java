package com.agnosticcms.web.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agnosticcms.web.dao.ModuleDao;
import com.agnosticcms.web.dto.ColumnType;
import com.agnosticcms.web.dto.ExternalModule;
import com.agnosticcms.web.dto.Module;
import com.agnosticcms.web.dto.ModuleColumn;
import com.agnosticcms.web.dto.ModuleHierarchy;
import com.agnosticcms.web.dto.form.ModuleInput;

/**
 * Service for work with Agnostic CMS modules
 */
@Service
public class ModuleService {

	@Autowired
	private ModuleDao moduleDao;
	
	/**
	 * {@link ModuleDao#getAllModules()}
	 */
	public List<Module> getAllModules() {
		return moduleDao.getAllModules();
	}
	
	/**
	 * {@link ModuleDao#getAllExternalModules()}
	 */
	public List<ExternalModule> getAllExternalModules() {
		return moduleDao.getAllExternalModules();
	}
	
	/**
	 * {@link ModuleDao#getModule(Long)}
	 */
	public Module getModule(Long id) {
		return moduleDao.getModule(id);
	}
	
	/**
	 * {@link ModuleDao#getModuleColumns(Long)}
	 */
	public List<ModuleColumn> getModuleColumns(Long moduleId) {
		return moduleDao.getModuleColumns(moduleId);
	}
	
	/**
	 * {@link ModuleDao#getParentModules(Long)}
	 */
	public List<Module> getParentModules(Long moduleId) {
		return moduleDao.getParentModules(moduleId);
	}
	
	/**
	 * {@link ModuleDao#getModuleHierarchies(Long)}
	 */
	public List<ModuleHierarchy> getModuleHierarchies(Long moduleId) {
		return moduleDao.getModuleHierarchies(moduleId);
	}
	
	/**
	 * @return All column type names separated by comma
	 */
	public String getAllColumnTypesAsString() {
		return StringUtils.join(Arrays.stream(ColumnType.values()).map(columnType -> columnType.toString()).collect(Collectors.toList()), ",");
	}
	
	/**
	 * Creates default {@link ModuleInput} by setting it with the default column values from database
	 * @param moduleColumns Module columns to fill the input with
	 * @return Module input filled with default column values
	 */
	public ModuleInput getDefaultModuleInput(List<ModuleColumn> moduleColumns) {
		ModuleInput moduleInput = new ModuleInput();
		moduleInput.setColumnValues(
				moduleColumns.stream().filter(mc -> mc.getDefaultValue() != null)
				.collect(Collectors.toMap(mc -> mc.getId(), mc -> mc.getDefaultValue()))
		);
		
		return moduleInput;
	}

	/**
	 * Checks whether there is a column of a type that is coupled with a file
	 * @param columns Columns to check for
	 * @return true if there is such a column, false otherwise
	 */
	public boolean containsFileColumns(List<ModuleColumn> columns) {
		return containsColumns(columns, ColumnType.IMAGE);
	}
	
	/**
	 * Checks whether there is a column of an HTML type
	 * @param columns Columns to check for
	 * @return true if there is such a column, false otherwise
	 */
	public boolean containsHTMLColumns(List<ModuleColumn> columns) {
		return containsColumns(columns, ColumnType.HTML);
	}
	
	/**
	 * Checks whether there is at least one column who complies with any of the given types
	 * @param columns Columns to search into
	 * @param columnTypes Column types to look for
	 * @return true if there is such a column, false otherwise
	 */
	private boolean containsColumns(List<ModuleColumn> columns, ColumnType... columnTypes) {
		for(ModuleColumn column : columns) {
			if(ArrayUtils.contains(columnTypes, column.getType())) {
				return true;
			}
		}
		
		return false;
	}

}
