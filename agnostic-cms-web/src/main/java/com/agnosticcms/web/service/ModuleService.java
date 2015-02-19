package com.agnosticcms.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agnosticcms.web.dao.ModuleDao;
import com.agnosticcms.web.dto.Module;

@Service
public class ModuleService {

	@Autowired
	private ModuleDao moduleDao;
	
	public List<Module> getAllModules() {
		return moduleDao.getAllModules();
	}
	
}
