package com.agnosticcms.web.controller;

import java.util.List;
import java.util.Map;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.agnosticcms.web.dto.Module;
import com.agnosticcms.web.dto.ModuleColumn;
import com.agnosticcms.web.exception.ResourceNotFoundException;
import com.agnosticcms.web.service.ModuleService;
import com.agnosticcms.web.service.ModuleTableService;
import com.agnosticcms.web.service.SessionService;

@Controller
@RequestMapping("module")
public class ModuleController extends RegisteredController {
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private ModuleService moduleService;
	
	@Autowired
	private ModuleTableService moduleTableService;
	
	@RequestMapping("/view/{id}")
	public String view(@PathVariable Long id, Model model) {
		
		Module module = moduleService.getModule(id);
		
		if(module == null) {
			throw new ResourceNotFoundException();
		}
		
		List<Module> parentModules = moduleService.getParentModules(id);
		List<ModuleColumn> columns = moduleService.getModuleColumns(id);
		Result<Record> rawRows = moduleTableService.getRows(module);
		List<String> foreignKeyNames = moduleTableService.getForeignKeyNames(parentModules);
		// 'list of values'
		Map<Integer, Map<Long, Object>> lovs = moduleTableService.getLovsForModule(module, parentModules, foreignKeyNames, rawRows);
		
		model.addAttribute("module", module);
		model.addAttribute("parentModules", parentModules);
		model.addAttribute("columns", columns);
		model.addAttribute("rows", rawRows.intoMaps());
		model.addAttribute("foreignKeyNames", foreignKeyNames);
		model.addAttribute("lovs", lovs);
		model.addAttribute("selectedModuleId", id);
		return "registered/body/module-view";
	}

}