package com.agnosticcms.web.controller;

import java.util.List;
import java.util.Map;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.agnosticcms.web.dto.Lov;
import com.agnosticcms.web.dto.Module;
import com.agnosticcms.web.dto.ModuleColumn;
import com.agnosticcms.web.dto.form.ModuleInput;
import com.agnosticcms.web.dto.form.ValidatableModuleInput;
import com.agnosticcms.web.exception.ResourceNotFoundException;
import com.agnosticcms.web.service.ModuleService;
import com.agnosticcms.web.service.ModuleTableService;
import com.agnosticcms.web.service.SessionService;
import com.agnosticcms.web.validation.ValidatableModelInputValidator;

@Controller
@RequestMapping("module")
public class ModuleController extends RegisteredController {
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private ModuleService moduleService;
	
	@Autowired
	private ModuleTableService moduleTableService;
	
	@Autowired
	private ValidatableModelInputValidator validatableModelInputValidator;
	
	@RequestMapping("/view/{id}")
	public String view(@PathVariable("id") Long moduleId, Model model) {
		
		Module module = getModule(moduleId);
		
		List<Module> parentModules = moduleService.getParentModules(moduleId);
		List<ModuleColumn> columns = moduleService.getModuleColumns(moduleId);
		Result<Record> rawRows = moduleTableService.getRows(module);
		List<String> foreignKeyNames = moduleTableService.getForeignKeyNames(parentModules);
		// 'list of values'
		Map<Integer, Map<Long, Object>> lovs = moduleTableService.getLovs(parentModules, foreignKeyNames, rawRows);
		
		model.addAttribute("module", module);
		model.addAttribute("parentModules", parentModules);
		model.addAttribute("columns", columns);
		model.addAttribute("rows", rawRows.intoMaps());
		model.addAttribute("foreignKeyNames", foreignKeyNames);
		model.addAttribute("lovs", lovs);
		model.addAttribute("selectedModuleId", moduleId);
		return "registered/body/module-view";
	}
	
	@RequestMapping(value = "/add/{id}", method = RequestMethod.GET)
	public String add(@PathVariable("id") Long moduleId, Model model) {
		
		Module module = getModule(moduleId);
		
		List<Module> parentModules = moduleService.getParentModules(moduleId);
		List<ModuleColumn> columns = moduleService.getModuleColumns(moduleId);
		Map<Integer, Lov> lovs = moduleTableService.getClassifierItems(parentModules);
		
		
		
		model.addAttribute("module", module);
		model.addAttribute("parentModules", parentModules);
		model.addAttribute("columns", columns);
		model.addAttribute("lovs", lovs);
		model.addAttribute("selectedModuleId", moduleId);
		model.addAttribute("command", new ModuleInput());
		return "registered/body/module-add";
	}
	
	@RequestMapping(value = "/add/{id}", method = RequestMethod.POST)
	public String saveAdd(
			@PathVariable("id") Long moduleId, 
			@ModelAttribute ModuleInput moduleInput,
			Model model,
			BindingResult result) {
		
		Module module = getModule(moduleId);
		
		ValidatableModuleInput validatableModuleInput = new ValidatableModuleInput();
		validatableModuleInput.setModuleInput(moduleInput);
		
		validatableModelInputValidator.validate(validatableModuleInput, result);
		
		if(result.hasErrors()) {
			List<Module> parentModules = moduleService.getParentModules(moduleId);
			List<ModuleColumn> columns = moduleService.getModuleColumns(moduleId);
			Map<Integer, Lov> lovs = moduleTableService.getClassifierItems(parentModules);
			
			model.addAttribute("module", module);
			model.addAttribute("parentModules", parentModules);
			model.addAttribute("columns", columns);
			model.addAttribute("lovs", lovs);
			model.addAttribute("selectedModuleId", moduleId);
			model.addAttribute("command", moduleInput);
			return "registered/body/module-add";
		} else {
			return "redirect:/module/view/" + moduleId;
		}
		
	}
	
	private Module getModule(Long id) {
		Module module =  moduleService.getModule(id);
		
		if(module == null) {
			throw new ResourceNotFoundException();
		}
		
		return module;
	}

}