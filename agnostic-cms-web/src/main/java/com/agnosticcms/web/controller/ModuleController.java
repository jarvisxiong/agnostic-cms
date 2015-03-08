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

import com.agnosticcms.web.dto.CmsTables;
import com.agnosticcms.web.dto.Lov;
import com.agnosticcms.web.dto.Module;
import com.agnosticcms.web.dto.ModuleColumn;
import com.agnosticcms.web.dto.ModuleHierarchy;
import com.agnosticcms.web.dto.form.ModuleInput;
import com.agnosticcms.web.dto.form.ValidatableModuleInput;
import com.agnosticcms.web.exception.ResourceNotFoundException;
import com.agnosticcms.web.service.FileService;
import com.agnosticcms.web.service.ModuleService;
import com.agnosticcms.web.service.ModuleTableService;
import com.agnosticcms.web.service.SessionService;
import com.agnosticcms.web.validation.ModuleInputAddValidator;
import com.agnosticcms.web.validation.ModuleInputUpdateValidator;

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
	private ModuleInputAddValidator moduleInputAddValidator;
	
	@Autowired
	private ModuleInputUpdateValidator moduleInputUpdateValidator;
	
	@Autowired
	private FileService fileService;
	
	@RequestMapping("/view/{moduleId}")
	public String view(@PathVariable Long moduleId, Model model) {
		
		Module module = selectModule(moduleId, model);
		
		List<Module> parentModules = moduleService.getParentModules(moduleId);
		List<ModuleColumn> columns = moduleService.getModuleColumns(moduleId);
		Result<Record> rawRows = moduleTableService.getRows(module);
		List<String> foreignKeyNames = moduleTableService.getForeignKeyColumnNames(parentModules);
		Map<Integer, Map<Long, Object>> lovs = moduleTableService.getLovs(parentModules, foreignKeyNames, rawRows);
		
		model.addAttribute("module", module);
		model.addAttribute("parentModules", parentModules);
		model.addAttribute("columns", columns);
		model.addAttribute("rows", rawRows.intoMaps());
		model.addAttribute("foreignKeyNames", foreignKeyNames);
		model.addAttribute("lovs", lovs);
		return "registered/body/module-view";
	}
	
	@RequestMapping("/view/{moduleId}/{itemId}")
	public String viewSingle(@PathVariable Long moduleId, @PathVariable Long itemId, Model model) {
		
		Module module = selectModule(moduleId, model);
		Record rawRow = selectRow(module, itemId);
		
		List<Module> parentModules = moduleService.getParentModules(moduleId);
		List<ModuleColumn> moduleColumns = moduleService.getModuleColumns(moduleId);
		Map<Integer, Object> lovItems = moduleTableService.getLovsSingleValue(parentModules, rawRow);
		
		model.addAttribute("module", module);
		model.addAttribute("itemId", itemId);
		model.addAttribute("row", rawRow.intoMap());
		model.addAttribute("parentModules", parentModules);
		model.addAttribute("columns", moduleColumns);
		model.addAttribute("lovItems", lovItems);
		model.addAttribute("activable", moduleId == CmsTables.MODULES.getModuleId());
		return "registered/body/module-view-single";
	}
	
	@RequestMapping("/delete/{moduleId}/{itemId}")
	public String delete(@PathVariable Long moduleId, @PathVariable Long itemId, Model model) {
		
		Module module = selectModule(moduleId, model);
		moduleTableService.deleteRow(module, itemId);
		
		return "redirect:/module/view/" + moduleId;
	}
	
	@RequestMapping(value = "/add/{moduleId}", method = RequestMethod.GET)
	public String add(@PathVariable Long moduleId, Model model) {
		
		Module module = selectModule(moduleId, model);
		
		List<Module> parentModules = moduleService.getParentModules(moduleId);
		List<ModuleColumn> columns = moduleService.getModuleColumns(moduleId);
		Map<Integer, Lov> lovs = moduleTableService.getClassifierItems(parentModules);
		ModuleInput moduleInput = moduleService.getDefaultModuleInput(columns);
		
		
		
		model.addAttribute("module", module);
		model.addAttribute("parentModules", parentModules);
		model.addAttribute("columns", columns);
		model.addAttribute("lovs", lovs);
		model.addAttribute("moduleInput", moduleInput);
		model.addAttribute("filesEnabled", moduleService.containsFileColumns(columns));
		model.addAttribute("wysiwygEnabled", moduleService.containsHTMLColumns(columns));
		return "registered/body/module-add-edit";
	}
	
	@RequestMapping(value = "/add/{moduleId}", method = RequestMethod.POST)
	public String saveAdd(
			@PathVariable Long moduleId, 
			@ModelAttribute ModuleInput moduleInput,
			Model model,
			BindingResult result) {
		
		Module module = selectModule(moduleId, model);
		
		List<Module> parentModules = moduleService.getParentModules(moduleId);
		List<ModuleColumn> moduleColumns = moduleService.getModuleColumns(moduleId);
		List<ModuleHierarchy> moduleHierarchies = moduleService.getModuleHierarchies(moduleId);
		
		ValidatableModuleInput validatableModuleInput = new ValidatableModuleInput();
		validatableModuleInput.setModuleInput(moduleInput);
		validatableModuleInput.setModuleColumns(moduleColumns);
		validatableModuleInput.setModuleHierarchies(moduleHierarchies);

		
		moduleInputAddValidator.validate(validatableModuleInput, result);
		
		if(result.hasErrors()) {
			
			Map<Integer, Lov> lovs = moduleTableService.getClassifierItems(parentModules);
			
			model.addAttribute("module", module);
			model.addAttribute("parentModules", parentModules);
			model.addAttribute("columns", moduleColumns);
			model.addAttribute("lovs", lovs);
			model.addAttribute("moduleInput", moduleInput);
			model.addAttribute("filesEnabled", moduleService.containsFileColumns(moduleColumns));
			model.addAttribute("wysiwygEnabled", moduleService.containsHTMLColumns(moduleColumns));
			return "registered/body/module-add-edit";
		} else {
			fileService.saveImages(module, moduleColumns, moduleInput);
			moduleTableService.saveModuleInput(module, moduleInput, parentModules, moduleColumns, null);
			return "redirect:/module/view/" + moduleId;
		}
		
	}
	
	@RequestMapping(value = "/edit/{moduleId}/{itemId}", method = RequestMethod.GET)
	public String edit(@PathVariable Long moduleId, @PathVariable Long itemId, Model model) {
		
		Module module = selectModule(moduleId, model);
		
		List<Module> parentModules = moduleService.getParentModules(moduleId);
		List<ModuleColumn> moduleColumns = moduleService.getModuleColumns(moduleId);
		Map<Integer, Lov> lovs = moduleTableService.getClassifierItems(parentModules);
		ModuleInput moduleInput = moduleTableService.getFilledModuleInput(module, parentModules, moduleColumns, itemId);
		
		model.addAttribute("module", module);
		model.addAttribute("parentModules", parentModules);
		model.addAttribute("columns", moduleColumns);
		model.addAttribute("lovs", lovs);
		model.addAttribute("moduleInput", moduleInput);
		model.addAttribute("editMode", true);
		model.addAttribute("filesEnabled", moduleService.containsFileColumns(moduleColumns));
		model.addAttribute("wysiwygEnabled", moduleService.containsHTMLColumns(moduleColumns));
		return "registered/body/module-add-edit";
	}
	
	@RequestMapping(value = "/edit/{moduleId}/{itemId}", method = RequestMethod.POST)
	public String saveEdit(
			@PathVariable Long moduleId,
			@PathVariable Long itemId,
			@ModelAttribute ModuleInput moduleInput,
			Model model,
			BindingResult result) {
		
		Module module = selectModule(moduleId, model);
		
		List<Module> parentModules = moduleService.getParentModules(moduleId);
		List<ModuleColumn> moduleColumns = moduleService.getModuleColumns(moduleId);
		List<ModuleHierarchy> moduleHierarchies = moduleService.getModuleHierarchies(moduleId);
		
		boolean filesEnabled = moduleService.containsFileColumns(moduleColumns);
		
		if(filesEnabled) {
			moduleTableService.populateWithFileColumnValues(module, itemId, moduleColumns, moduleInput.getColumnValues());
		}
		
		ValidatableModuleInput validatableModuleInput = new ValidatableModuleInput();
		validatableModuleInput.setModuleInput(moduleInput);
		validatableModuleInput.setModuleColumns(moduleColumns);
		validatableModuleInput.setModuleHierarchies(moduleHierarchies);

		
		moduleInputUpdateValidator.validate(validatableModuleInput, result);
		
		if(result.hasErrors()) {
			
			Map<Integer, Lov> lovs = moduleTableService.getClassifierItems(parentModules);
			
			model.addAttribute("module", module);
			model.addAttribute("parentModules", parentModules);
			model.addAttribute("columns", moduleColumns);
			model.addAttribute("lovs", lovs);
			model.addAttribute("moduleInput", moduleInput);
			model.addAttribute("editMode", true);
			model.addAttribute("filesEnabled", filesEnabled);
			model.addAttribute("wysiwygEnabled", moduleService.containsHTMLColumns(moduleColumns));
			return "registered/body/module-add-edit";
		} else {
			fileService.saveImages(module, moduleColumns, moduleInput);
			moduleTableService.saveModuleInput(module, moduleInput, parentModules, moduleColumns, itemId);
			return "redirect:/module/view/" + moduleId;
		}
		
	}
	
	@RequestMapping(value = "/activation/{moduleId}/{activate}", method = RequestMethod.GET)
	public String activation(@PathVariable Long moduleId, @PathVariable Boolean activate) {
		Module module = selectModule(moduleId);
		
		if(activate) {
			moduleTableService.activate(module);
		} else {
			moduleTableService.deactivate(module);
		}

		return "redirect:/module/view/" + CmsTables.MODULES.getModuleId();
	}
	
	private Module selectModule(Long id, Model model) {
		Module module =  moduleService.getModule(id);
		
		if(module == null) {
			throw new ResourceNotFoundException();
		}
		
		if(model != null) {
			model.addAttribute("selectedModuleId", id);
		}
		
		return module;
	}
	
	private Module selectModule(Long id) {
		return selectModule(id, null);
	}
	
	private Record selectRow(Module module, Long itemId) {
		Record rawRow = moduleTableService.getRow(module, itemId);
		
		if(rawRow == null) {
			throw new ResourceNotFoundException();
		}
		
		return rawRow;
	}
	
	

}