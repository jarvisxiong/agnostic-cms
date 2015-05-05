package com.agnosticcms.web.validation;

import org.springframework.stereotype.Component;

import com.agnosticcms.web.dto.ModuleColumn;

/**
 * Validator for adding a new module element
 */
@Component
public class ModuleInputAddValidator extends ModuleInputValidator {

	@Override
	protected boolean isColumnProcessable(ModuleColumn moduleColumn) {
		return true;
	}

}
