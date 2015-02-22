package com.agnosticcms.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agnosticcms.web.dao.ModuleDao;
import com.agnosticcms.web.dto.Module;
import com.agnosticcms.web.dto.ModuleColumn;

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
}
