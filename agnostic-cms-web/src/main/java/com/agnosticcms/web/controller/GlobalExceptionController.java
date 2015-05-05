package com.agnosticcms.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.agnosticcms.web.exception.ResourceNotFoundException;

/**
 * Controller for global exception handling in Spring MVC controllers
 */
@ControllerAdvice
public class GlobalExceptionController {
	
	
	/**
	 * Returns a 404 JSP page for certain exceptions
	 */
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({ResourceNotFoundException.class, NoHandlerFoundException.class})
	public String notFound() {
        return "404";
	}
	
}
