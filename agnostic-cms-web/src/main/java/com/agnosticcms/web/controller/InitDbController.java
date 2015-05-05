package com.agnosticcms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.agnosticcms.web.service.InitDbService;

/**
 * Controller for database structure initialization
 */
@Controller
public class InitDbController {

	@Autowired
	private InitDbService initDbService;
	
	/**
	 * Initialises database and shows either success message or message that database has already been initialised
	 */
	@RequestMapping("/initdb")
	public String initdb(Model model) {
		
		String messageCode = initDbService.initDb() ? "initdb.success" : "initdb.alreadyInitialised";
		model.addAttribute("messageCode", messageCode);
		return "db-init-success";
	}

}