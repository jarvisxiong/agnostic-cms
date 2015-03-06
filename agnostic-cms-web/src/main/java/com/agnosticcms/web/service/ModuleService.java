package com.agnosticcms.web.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agnosticcms.web.dao.ModuleDao;
import com.agnosticcms.web.dto.ColumnType;
import com.agnosticcms.web.dto.Module;
import com.agnosticcms.web.dto.ModuleColumn;
import com.agnosticcms.web.dto.ModuleHierarchy;
import com.agnosticcms.web.dto.form.ModuleInput;

@Service
public class ModuleService {

	@Autowired
	private ModuleDao moduleDao;
	
	
	public List<Module> getAllModules() {
		return moduleDao.getAllModules();
	}
	
	public Module getModule(Long id) {
		return moduleDao.getModule(id);
	}
	
	public List<ModuleColumn> getModuleColumns(Long moduleId) {
		return moduleDao.getModuleColumns(moduleId);
	}
	
	public List<Module> getParentModules(Long moduleId) {
		return moduleDao.getParentModules(moduleId);
	}
	
	public List<ModuleHierarchy> getModuleHierarchies(Long moduleId) {
		return moduleDao.getModuleHierarchies(moduleId);
	}
	
	public String getAllColumnTypesAsString() {
		return StringUtils.join(Arrays.stream(ColumnType.values()).map(columnType -> columnType.toString()).collect(Collectors.toList()), ",");
	}
	
	public ModuleInput getDefaultModuleInput(List<ModuleColumn> moduleColumns) {
		ModuleInput moduleInput = new ModuleInput();
		moduleInput.setColumnValues(
				moduleColumns.stream().filter(mc -> mc.getDefaultValue() != null)
				.collect(Collectors.toMap(mc -> mc.getId(), mc -> mc.getDefaultValue()))
		);
		
		return moduleInput;
	}

	public Object containsFileColumns(List<ModuleColumn> columns) {
		
		for(ModuleColumn column : columns) {
			ColumnType columnType = column.getType();
			
			if(columnType == ColumnType.IMAGE) {
				return true;
			}
		}
		
		return false;
	}

}
