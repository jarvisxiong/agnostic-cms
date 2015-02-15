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
public class HelloWorldController {
	
	@Autowired
	private SessionService sessionService;
	
	@RequestMapping("/hello")
	public String hello(@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "value", required = false) String value,
			@RequestHeader Map<String, String> headers, Model model, HttpServletResponse response, HttpServletRequest request) {
		
		model.addAttribute("name", request.getHeader("X-Session"));
		
		if(StringUtils.isNotEmpty(key)) {
			sessionService.setAttribute(key, value);
		}
		
		return "index";
	}

}