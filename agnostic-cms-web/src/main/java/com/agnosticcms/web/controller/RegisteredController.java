package com.agnosticcms.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.agnosticcms.web.dto.ExternalModule;
import com.agnosticcms.web.dto.Module;
import com.agnosticcms.web.exception.DataIntegrityException;
import com.agnosticcms.web.service.ModuleService;
import com.agnosticcms.web.service.SessionService;

public abstract class RegisteredController {

	private static final String ATTR_MODULES = "modules";
	private static final String ATTR_EXTERNAL_MODULES = "externalModules";
	private static final String ATTR_USERNAME = "username";
	
	@Autowired
	protected ModuleService moduleService;
	
	@Autowired
	protected SessionService sessionService;
	
	
	@ModelAttribute(ATTR_MODULES)
	public List<Module> getAllModules() {
		return moduleService.getAllModules();
	}
	
	@ModelAttribute(ATTR_EXTERNAL_MODULES)
	public List<ExternalModule> getAllExternalModules() {
		return moduleService.getAllExternalModules();
	}
	
	@ModelAttribute(ATTR_USERNAME)
	public String getUsername() {
		return sessionService.getAttribute("cms-user");
	}
	
	/**
	 * This is needed because @ModelAttribute is not taken into account in
	 * @ExceptionHandler views
	 */
	protected void populateModuleAndView(ModelAndView modelAndView) {
		modelAndView.addObject(ATTR_MODULES, getAllModules());
		modelAndView.addObject(ATTR_EXTERNAL_MODULES, getAllExternalModules());
		modelAndView.addObject(ATTR_USERNAME, getAllModules());
	}
	
	@RequestMapping("/logout")
	public String logout() {
		sessionService.removeAttribute("cms-user");
		return "redirect:/";
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
