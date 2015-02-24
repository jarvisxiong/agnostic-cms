package com.agnosticcms.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.agnosticcms.web.dto.form.ValidatableModuleInput;

@Component
public class ValidatableModelInputValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ValidatableModuleInput.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		ValidatableModuleInput input = (ValidatableModuleInput) target;
		
		errors.rejectValue("columnValues[1]", "validation.required");
	}

}
