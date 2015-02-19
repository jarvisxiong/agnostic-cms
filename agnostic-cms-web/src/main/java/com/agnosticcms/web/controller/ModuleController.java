package com.agnosticcms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.agnosticcms.web.service.SessionService;

@Controller
@RequestMapping("module")
public class ModuleController extends RegisteredController {
	
	@Autowired
	private SessionService sessionService;
	
	@RequestMapping("/view/{id}")
	public String view(@PathVariable Long id, Model model) {
		model.addAttribute("selectedModuleId", id);
		return "registered/body/model-view";
	}

}