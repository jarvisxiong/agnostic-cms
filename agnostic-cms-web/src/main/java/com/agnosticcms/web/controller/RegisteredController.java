package com.agnosticcms.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.agnosticcms.web.dto.Module;
import com.agnosticcms.web.service.ModuleService;

public abstract class RegisteredController {

	@Autowired
	protected ModuleService moduleService;
	
	@ModelAttribute("modules")
	public List<Module> getAllModules() {
		return moduleService.getAllModules();
	}
	
}
