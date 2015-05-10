package com.agnosticcms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.agnosticcms.web.service.SessionService;

/**
 * Controller for displaying home page of Agnostic CMS
 */
@Controller
public class HomeController extends RegisteredController {
	
	@Autowired
	private SessionService sessionService;
	
	/**
	 * Shows greeting page for the user
	 */
	@RequestMapping({"/", "/home"})
	public String home() {
		return "registered/body/home";
	}

}