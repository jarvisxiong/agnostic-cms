package com.agnosticcms.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.agnosticcms.web.dto.Module;
import com.agnosticcms.web.exception.DataIntegrityException;
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
	
	@ExceptionHandler(DataIntegrityException.class)
	public ModelAndView dbIntegrityViolation(DataIntegrityException exception) {
		ModelAndView modelAndView = new ModelAndView("registered/body/error");
		populateModuleAndView(modelAndView);
		
		String i18nMessage = exception.getI18nMessageCode();
		
		if(i18nMessage != null) {
			modelAndView.addObject("errorMsgCode", i18nMessage);
		} else {
			modelAndView.addObject("errorMsg", exception.getMessage());
		}
       
        return modelAndView;
	}
	
}
