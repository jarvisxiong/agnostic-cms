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

import com.agnosticcms.web.dto.CmsTable;
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

/**
 * This controller supports all activities that are associated with modules
 */
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
	
	/**
	 * Displays a list of module elements to the user
	 */
	@RequestMapping("/view/{moduleId}")
	public String view(@PathVariable Long moduleId, Model model) {
		
		Module module = selectModule(moduleId, model);
		
		List<Module> parentModules = moduleService.getParentModules(moduleId);
		List<ModuleColumn> columns = moduleService.getModuleColumns(moduleId);
		// Full module element result set
		Result<Record> rawRows = moduleTableService.getRows(module);
		List<String> foreignKeyNames = moduleTableService.getForeignKeyColumnNames(parentModules);
		// List of values to show in UI as references to parent modules
		Map<Integer, Map<Long, Object>> lovs = moduleTableService.getLovs(parentModules, foreignKeyNames, rawRows);
		
		model.addAttribute("module", module);
		model.addAttribute("parentModules", parentModules);
		model.addAttribute("columns", columns);
		model.addAttribute("rows", rawRows.intoMaps());
		model.addAttribute("foreignKeyNames", foreignKeyNames);
		model.addAttribute("lovs", lovs);
		return "registered/body/module-view";
	}
	
	/**
	 * Displays details of a single module element
	 */
	@RequestMapping("/view/{moduleId}/{itemId}")
	public String viewSingle(@PathVariable Long moduleId, @PathVariable Long itemId, Model model) {
		
		Module module = selectModule(moduleId, model);
		// module element details
		Record rawRow = selectRow(module, itemId);
		
		List<Module> parentModules = moduleService.getParentModules(moduleId);
		List<ModuleColumn> moduleColumns = moduleService.getModuleColumns(moduleId);
		// List of values to show in UI as references to parent modules
		Map<Integer, Object> lovItems = moduleTableService.getLovsSingleValue(parentModules, rawRow);
		
		model.addAttribute("module", module);
		model.addAttribute("itemId", itemId);
		model.addAttribute("row", rawRow.intoMap());
		model.addAttribute("parentModules", parentModules);
		model.addAttribute("columns", moduleColumns);
		model.addAttribute("lovItems", lovItems);
		// If current module is "Modules", allow activation of it's elements
		model.addAttribute("activable", moduleId == CmsTable.MODULES.getModuleId());
		return "registered/body/module-view-single";
	}
	
	/**
	 * Removes module element from database
	 */
	@RequestMapping("/delete/{moduleId}/{itemId}")
	public String delete(@PathVariable Long moduleId, @PathVariable Long itemId, Model model) {
		
		Module module = selectModule(moduleId, model);
		moduleTableService.deleteRow(module, itemId);
		
		// Redirect back to the module element list view
		return "redirect:/module/view/" + moduleId;
	}
	
	/**
	 * Displays a form for addition of a new module element
	 */
	@RequestMapping(value = "/add/{moduleId}", method = RequestMethod.GET)
	public String add(@PathVariable Long moduleId, Model model) {
		
		Module module = selectModule(moduleId, model);
		
		List<Module> parentModules = moduleService.getParentModules(moduleId);
		List<ModuleColumn> columns = moduleService.getModuleColumns(moduleId);
		// List of values to display to the user for parent modules
		Map<Integer, Lov> lovs = moduleTableService.getClassifierItems(parentModules);
		// Sets default form values
		ModuleInput moduleInput = moduleService.getDefaultModuleInput(columns);
		
		
		model.addAttribute("module", module);
		model.addAttribute("parentModules", parentModules);
		model.addAttribute("columns", columns);
		model.addAttribute("lovs", lovs);
		model.addAttribute("moduleInput", moduleInput);
		// Enabling file upload support only if we need it
		model.addAttribute("filesEnabled", moduleService.containsFileColumns(columns));
		// Enabling wysiwyg support only if we need it
		model.addAttribute("wysiwygEnabled", moduleService.containsHTMLColumns(columns));
		return "registered/body/module-add-edit";
	}
	
	/**
	 * Saves a new module element to the database
	 */
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
		
		// Sets information needed for validation process
		ValidatableModuleInput validatableModuleInput = new ValidatableModuleInput();
		validatableModuleInput.setModuleInput(moduleInput);
		validatableModuleInput.setModuleColumns(moduleColumns);
		validatableModuleInput.setModuleHierarchies(moduleHierarchies);

		// Validating posted values against module's metamodel
		moduleInputAddValidator.validate(validatableModuleInput, result);
		
		// If validation has failed, redisplay the form with validation errors
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
			
		// If validation has succeeded, save module element data and uploaded files
		} else {
			fileService.saveImages(module, moduleColumns, moduleInput);
			moduleTableService.saveModuleInput(module, moduleInput, parentModules, moduleColumns, null);
			return "redirect:/module/view/" + moduleId;
		}
	}
	
	/**
	 * Displays a form for editing an existing module element
	 */
	@RequestMapping(value = "/edit/{moduleId}/{itemId}", method = RequestMethod.GET)
	public String edit(@PathVariable Long moduleId, @PathVariable Long itemId, Model model) {
		
		Module module = selectModule(moduleId, model);
		
		List<Module> parentModules = moduleService.getParentModules(moduleId);
		List<ModuleColumn> moduleColumns = moduleService.getModuleColumns(moduleId);
		// List of values to display to the user for parent modules
		Map<Integer, Lov> lovs = moduleTableService.getClassifierItems(parentModules);
		ModuleInput moduleInput = moduleTableService.getFilledModuleInput(module, parentModules, moduleColumns, itemId);
		
		model.addAttribute("module", module);
		model.addAttribute("parentModules", parentModules);
		model.addAttribute("columns", moduleColumns);
		model.addAttribute("lovs", lovs);
		model.addAttribute("moduleInput", moduleInput);
		// As we use same jsp file both for module element addition and removal, we set editMode flag to true
		model.addAttribute("editMode", true);
		// Enabling file upload support only if we need it
		model.addAttribute("filesEnabled", moduleService.containsFileColumns(moduleColumns));
		// Enabling wysiwyg support only if we need it
		model.addAttribute("wysiwygEnabled", moduleService.containsHTMLColumns(moduleColumns));
		return "registered/body/module-add-edit";
	}
	
	/**
	 * Stores changes for an existing module element
	 */
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
		
		// Whether file upload support should be enabled
		boolean filesEnabled = moduleService.containsFileColumns(moduleColumns);
		
		// If file upload is to be enabled add current database values for file columns as they are not posted back by user
		if(filesEnabled) {
			moduleTableService.populateWithFileColumnValues(module, itemId, moduleColumns, moduleInput.getColumnValues());
		}
		
		// Sets information needed for validation process
		ValidatableModuleInput validatableModuleInput = new ValidatableModuleInput();
		validatableModuleInput.setModuleInput(moduleInput);
		validatableModuleInput.setModuleColumns(moduleColumns);
		validatableModuleInput.setModuleHierarchies(moduleHierarchies);

		// Validate module element changes against module's metamodel
		moduleInputUpdateValidator.validate(validatableModuleInput, result);
		
		// If validation has failed, redisplay the form with validation errors
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
		
		// If validation has succeeded, save module element data changes and uploaded files
		} else {
			fileService.saveImages(module, moduleColumns, moduleInput);
			moduleTableService.saveModuleInput(module, moduleInput, parentModules, moduleColumns, itemId);
			return "redirect:/module/view/" + moduleId;
		}
		
	}
	
	/**
	 * Activates or de-activates given module
	 */
	@RequestMapping(value = "/activation/{moduleId}/{activate}", method = RequestMethod.GET)
	public String activation(@PathVariable Long moduleId, @PathVariable Boolean activate) {
		Module module = selectModule(moduleId);
		
		if(activate) {
			moduleTableService.activate(module);
		} else {
			moduleTableService.deactivate(module);
		}

		// Redirects to all modules view
		return "redirect:/module/view/" + CmsTable.MODULES.getModuleId();
	}
	
	/**
	 * Retrieves a module data from the database and adds selected module id attribute to the model
	 * @param id Id of module to retrieve
	 * @param model Spring MVC model element retrieved from controller's method input params
	 * @return Retrieved module
	 * @throws ResourceNotFoundException if module with the given id does not exist
	 */
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
	
	/**
	 * Retrieves module details without setting a model attribute
	 * @see #selectModule(Long, Model)
	 */
	private Module selectModule(Long id) {
		return selectModule(id, null);
	}
	
	/**
	 * Retrieves a module element data from the database
	 * @param module Module to retrieve the record from
	 * @param itemId Id of th item to retrieve
	 * @return A single module element data
	 * @throws ResourceNotFoundException if row doesn't exist
	 */
	private Record selectRow(Module module, Long itemId) {
		Record rawRow = moduleTableService.getRow(module, itemId);
		
		if(rawRow == null) {
			throw new ResourceNotFoundException();
		}
		
		return rawRow;
	}
	
	

}