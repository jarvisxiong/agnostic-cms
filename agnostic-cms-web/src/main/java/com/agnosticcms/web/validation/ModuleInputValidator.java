package com.agnosticcms.web.validation;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.agnosticcms.web.dto.ColumnType;
import com.agnosticcms.web.dto.ModuleColumn;
import com.agnosticcms.web.dto.ModuleHierarchy;
import com.agnosticcms.web.dto.form.ModuleInput;
import com.agnosticcms.web.dto.form.ValidatableModuleInput;
import com.agnosticcms.web.exception.TypeConversionException;
import com.agnosticcms.web.service.ColumnTypeService;

public abstract class ModuleInputValidator implements Validator {

	private static final String CODE_REQUIRED = "validation.required";
	private static final String CODE_INVALID_VALUE = "validation.value.invalid";
	private static final String CODE_INVALID_NUMBER = "validation.number.invalid";
	private static final String CODE_TOO_LONG = "validation.tooLong";
	
	private static final String CODE_FILE_TOO_BIG = "validation.file.tooBig";
	private static final String CODE_FILE_WRONG_EXT = "validation.file.wrongExt";
	private static final String CODE_FILE_ILLEGAL_FILE_NAME = "validation.file.illegalFileName";
	
	private static final Set<String> IMG_EXTENSIONS = new HashSet<>();
	
	static {
		IMG_EXTENSIONS.add("png");
		IMG_EXTENSIONS.add("jpg");
		IMG_EXTENSIONS.add("jpeg");
		IMG_EXTENSIONS.add("gif");
		IMG_EXTENSIONS.add("bmp");
	}
	
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
		
		Map<Integer, Long> lovValues = moduleInput.getLovValues();
		int i = 0;
		for(ModuleHierarchy moduleHierarchy : validatableModuleInput.getModuleHierarchies()) {
			if(moduleHierarchy.getMandatory()) {
				Long value = lovValues.get(i);
				if(value == null) {
					errors.rejectValue(getLovValuesFieldName(i), CODE_REQUIRED);
				}
			}
			
			i++;
		}
		
		Map<Long, String> columnValues = moduleInput.getColumnValues();
		Map<Long, MultipartFile> files = moduleInput.getFiles();
		for(ModuleColumn moduleColumn : validatableModuleInput.getModuleColumns()) {
			
			if(isColumnProcessable(moduleColumn)) {
				Long columnId = moduleColumn.getId();
				ColumnType columnType = moduleColumn.getType();
				String value = columnValues.get(columnId);
				
				
				if(columnType == ColumnType.IMAGE) {
					
					String fieldName = getImagesFieldName(columnId);
					
					MultipartFile multipartFile = files.get(columnId);
					if(multipartFile == null || multipartFile.isEmpty()) {
						if(moduleColumn.getNotNull() && StringUtils.isEmpty(value)) {
							errors.rejectValue(fieldName, CODE_REQUIRED);
						}
					} else {
						
						String originalFilename = multipartFile.getOriginalFilename();
						
						if(originalFilename == null || originalFilename.startsWith(".") || originalFilename.startsWith("~")) {
							errors.rejectValue(fieldName, CODE_FILE_ILLEGAL_FILE_NAME);
						}
						
						String extension = FilenameUtils.getExtension(originalFilename);
						if(!IMG_EXTENSIONS.contains(extension.toLowerCase())) {
							errors.rejectValue(fieldName, CODE_FILE_WRONG_EXT, new Object[] {StringUtils.join(IMG_EXTENSIONS, ", ")}, null);
						}
						
						Integer maxSize = moduleColumn.getSize();
						if(maxSize != null) {
							if(maxSize < multipartFile.getSize()) {
								errors.rejectValue(fieldName, CODE_FILE_TOO_BIG, new Object[] {maxSize}, null);
							}
						}
					}
					
					
					
				} else {
					
					String fieldName = getColumnValuesFieldName(columnId);
					
					if((StringUtils.isEmpty(value) && columnType != ColumnType.BOOL)) {
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
						case ENUM:
							String[] enumValues = StringUtils.split(moduleColumn.getTypeInfo(), ",");
							if(!ArrayUtils.contains(enumValues, convertedValue)) {
								errors.rejectValue(fieldName, CODE_INVALID_VALUE);
							}
						default:
						}
						
					}
					
				}
				
				
			}
			
			
		}
		
	}
	
	protected abstract boolean isColumnProcessable(ModuleColumn moduleColumn);
	
	private String getLovValuesFieldName(Integer id) {
		return getFieldName("lovValues", id);
	}
	
	private String getColumnValuesFieldName(Long id) {
		return getFieldName("columnValues", id);
	}
	
	private String getImagesFieldName(Long id) {
		return getFieldName("files", id);
	}
	
	private String getFieldName(String baseName, Number id) {
		StringBuilder sb = new StringBuilder();
		sb.append(baseName);
		sb.append("[");
		sb.append(id);
		sb.append("]");
		return sb.toString();
	}

}
