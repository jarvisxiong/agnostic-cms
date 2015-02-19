package com.agnosticcms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.agnosticcms.web.service.SessionService;

@Controller
public class HomeController extends RegisteredController {
	
	@Autowired
	private SessionService sessionService;
	
	@RequestMapping("/home")
	public String hello() {
		
		return "registered/body/home";
	}

}