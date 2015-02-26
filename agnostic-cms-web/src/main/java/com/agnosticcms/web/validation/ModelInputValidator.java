package com.agnosticcms.web.validation;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.agnosticcms.web.dto.ColumnType;
import com.agnosticcms.web.dto.ModuleColumn;
import com.agnosticcms.web.dto.ModuleHierarchy;
import com.agnosticcms.web.dto.form.ModuleInput;
import com.agnosticcms.web.dto.form.ValidatableModuleInput;
import com.agnosticcms.web.exception.TypeConversionException;
import com.agnosticcms.web.service.ColumnTypeService;

@Component
public class ModelInputValidator implements Validator {

	private static final String CODE_REQUIRED = "validation.required";
	private static final String CODE_INVALID_VALUE = "validation.value.invalid";
	private static final String CODE_INVALID_NUMBER = "validation.number.invalid";
	private static final String CODE_TOO_LONG = "validation.tooLong";
	
	@Autowired
	private ColumnTypeService columnTypeService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return ValidatableModuleInput.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		ValidatableModuleInput validatableModuleInput = (ValidatableModuleInput) target;
		ModuleInput moduleInput = validatableModuleInput.getModuleInput();
		
		Map<Long, Long> lovValues = moduleInput.getLovValues();
		for(ModuleHierarchy moduleHierarchy : validatableModuleInput.getModuleHierarchies()) {
			if(moduleHierarchy.getMandatory()) {
				Long parentModuleId = moduleHierarchy.getModuleId();
				Long value = lovValues.get(parentModuleId);
				if(value == null) {
					errors.rejectValue(getLovValuesFieldName(parentModuleId), CODE_REQUIRED);
				}
			}
		}
		
		Map<Long, String> columnValues = moduleInput.getColumnValues();
		for(ModuleColumn moduleColumn : validatableModuleInput.getModuleColumns()) {
			Long columnId = moduleColumn.getId();
			ColumnType columnType = moduleColumn.getType();
			String value = columnValues.get(columnId);
			String fieldName = getColumnValuesFieldName(columnId);
			
			if(StringUtils.isEmpty(value) && columnType != ColumnType.BOOL) {
				if(moduleColumn.getNotNull()) {
					errors.rejectValue(fieldName, CODE_REQUIRED);
				}
			} else {
				
				Object convertedValue;
				
				try {
					convertedValue = columnTypeService.parseFromString(value, columnType);
				} catch (TypeConversionException e) {
					
					String errorCode;
					
					switch (columnType) {
					case INT:
					case LONG:
						errorCode = CODE_INVALID_NUMBER;
						break;
					default:
						errorCode = CODE_INVALID_VALUE;
					}
					
					errors.rejectValue(fieldName, errorCode);
					continue;
				}
				
				
				switch (columnType) {
				case STRING:
					
					Integer size = moduleColumn.getSize();
					
					if(size != null && size < ((String) convertedValue).length()) {
						errors.rejectValue(fieldName, CODE_TOO_LONG, new Object[] {size}, null);
						continue;
					}
					break;
				default:
				}
				
			}
		}
		
	}
	
	
	
	private String getLovValuesFieldName(Long id) {
		return getFieldName("lovValues", id);
	}
	
	private String getColumnValuesFieldName(Long id) {
		return getFieldName("columnValues", id);
	}
	
	private String getFieldName(String baseName, Long id) {
		StringBuilder sb = new StringBuilder();
		sb.append(baseName);
		sb.append("[");
		sb.append(id);
		sb.append("]");
		return sb.toString();
	}

}
