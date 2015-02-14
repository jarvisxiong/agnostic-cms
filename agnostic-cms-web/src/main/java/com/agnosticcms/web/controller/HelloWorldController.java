package com.agnosticcms.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloWorldController {
	
	@RequestMapping("/hello")
	public String hello(@RequestParam(value = "name", required = false, defaultValue = "World") String name,
			@RequestHeader Map<String, String> headers, Model model, HttpServletResponse response) {
		
		if("tomsadd".equals(name)) {
			response.setHeader("X-Replace-Session", "toms=feldmanis");
		}
		
		if("tomsremove".equals(name)) {
			response.setHeader("X-Replace-Session", "toms=");
		}
		
		model.addAttribute("name", name);
		
		return "index";
	}

}