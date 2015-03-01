package com.agnosticcms.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.agnosticcms.web.dto.Module;
import com.agnosticcms.web.service.ModuleService;

public abstract class RegisteredController {

	private static final String ATTR_MODULES = "modules";
	
	@Autowired
	protected ModuleService moduleService;
	
	@ModelAttribute(ATTR_MODULES)
	public List<Module> getAllModules() {
		return moduleService.getAllModules();
	}
	
	/**
	 * This is needed because @ModelAttribute is not taken into account in
	 * @ExceptionHandler views
	 */
	protected void populateModuleAndView(ModelAndView modelAndView) {
		modelAndView.addObject(ATTR_MODULES, getAllModules());
	}
	
}
