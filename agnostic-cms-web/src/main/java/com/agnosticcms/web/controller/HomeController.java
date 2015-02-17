package com.agnosticcms.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.agnosticcms.web.service.SessionService;

@Controller
public class HomeController {
	
	@Autowired
	private SessionService sessionService;
	
	@RequestMapping("/home")
	public String hello() {
		
		return "registered/body/home";
	}

}