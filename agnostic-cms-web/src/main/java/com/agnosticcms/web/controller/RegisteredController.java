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

/**
 * Parent controller for all controllers for logged in users
 */
public abstract class RegisteredController {

	private static final String ATTR_MODULES = "modules";
	private static final String ATTR_EXTERNAL_MODULES = "externalModules";
	private static final String ATTR_USERNAME = "username";
	private static final String SESSION_ATTR_USER = "cms-user";
	
	@Autowired
	protected ModuleService moduleService;
	
	@Autowired
	protected SessionService sessionService;
	
	
	/**
	 * @return List of all modules
	 */
	@ModelAttribute(ATTR_MODULES)
	public List<Module> getAllModules() {
		return moduleService.getAllModules();
	}
	
	/**
	 * @return List of all external modules
	 */
	@ModelAttribute(ATTR_EXTERNAL_MODULES)
	public List<ExternalModule> getAllExternalModules() {
		return moduleService.getAllExternalModules();
	}
	
	/**
	 * @return Currently logged-in username from session
	 */
	@ModelAttribute(ATTR_USERNAME)
	public String getUsername() {
		return sessionService.getAttribute(SESSION_ATTR_USER);
	}
	
	/**
	 * Adds basic properties to @ModelAndView<br>
	 * This is needed because @ModelAttribute is not taken into account in @ExceptionHandler views
	 * @param modelAndView @ModelAndView to add the properties to
	 */
	protected void populateModuleAndView(ModelAndView modelAndView) {
		modelAndView.addObject(ATTR_MODULES, getAllModules());
		modelAndView.addObject(ATTR_EXTERNAL_MODULES, getAllExternalModules());
		modelAndView.addObject(ATTR_USERNAME, getUsername());
	}
	
	/**
	 * Logs out the current user by removing username attribute from session
	 */
	@RequestMapping("/logout")
	public String logout() {
		sessionService.removeAttribute(SESSION_ATTR_USER);
		return "redirect:/";
	}
	
	/**
	 * Handles @DataIntegrityException so that an error message is displayed to the user
	 */
	@ExceptionHandler(DataIntegrityException.class)
	public ModelAndView dbIntegrityViolation(DataIntegrityException exception) {
		ModelAndView modelAndView = new ModelAndView("registered/body/error");
		populateModuleAndView(modelAndView);
		
		String i18nMessage = exception.getI18nMessageCode();
		
		// if exception contains displayable error message code, show it
		if(i18nMessage != null) {
			modelAndView.addObject("errorMsgCode", i18nMessage);
		// else show raw exception message
		} else {
			modelAndView.addObject("errorMsg", exception.getMessage());
		}
       
        return modelAndView;
	}
	
}
