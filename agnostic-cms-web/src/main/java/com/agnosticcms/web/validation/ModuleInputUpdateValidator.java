package com.agnosticcms.web.validation;

import org.springframework.stereotype.Component;

import com.agnosticcms.web.dto.ModuleColumn;

/**
 * Validator for updating an existing module element
 */
@Component
public class ModuleInputUpdateValidator extends ModuleInputValidator {

	@Override
	protected boolean isColumnProcessable(ModuleColumn moduleColumn) {
		return moduleColumn.getShowInEdit();
	}

}
