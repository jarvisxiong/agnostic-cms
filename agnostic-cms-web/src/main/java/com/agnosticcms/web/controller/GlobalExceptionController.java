package com.agnosticcms.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.agnosticcms.web.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionController {
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({ResourceNotFoundException.class, NoHandlerFoundException.class})
	public String notFound() {
        return "404";
	}
	
}
