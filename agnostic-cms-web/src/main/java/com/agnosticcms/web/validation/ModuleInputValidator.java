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

/**
 * Abstract validator both for updating and inserting module elements
 */
public abstract class ModuleInputValidator implements Validator {

	// validation error i18n codes
	private static final String CODE_REQUIRED = "validation.required";
	private static final String CODE_INVALID_VALUE = "validation.value.invalid";
	private static final String CODE_INVALID_NUMBER = "validation.number.invalid";
	private static final String CODE_TOO_LONG = "validation.tooLong";
	
	private static final String CODE_FILE_TOO_BIG = "validation.file.tooBig";
	private static final String CODE_FILE_WRONG_EXT = "validation.file.wrongExt";
	private static final String CODE_FILE_ILLEGAL_FILE_NAME = "validation.file.illegalFileName";
	
	/**
	 * Supported image extensions
	 */
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
		// Checks that all mandatory LOV values are submitted
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
		// Go through all the columns of a module
		for(ModuleColumn moduleColumn : validatableModuleInput.getModuleColumns()) {
			
			// Continue only if the column should be processed
			if(isColumnProcessable(moduleColumn)) {
				Long columnId = moduleColumn.getId();
				ColumnType columnType = moduleColumn.getType();
				String value = columnValues.get(columnId);
				
				
				if(columnType == ColumnType.IMAGE) {
					
					String fieldName = getImagesFieldName(columnId);
					
					MultipartFile multipartFile = files.get(columnId);
					if(multipartFile == null || multipartFile.isEmpty()) {
						// Reject if mandatory file has not been uploaded
						if(moduleColumn.getNotNull() && StringUtils.isEmpty(value)) {
							errors.rejectValue(fieldName, CODE_REQUIRED);
						}
					} else {
						
						String originalFilename = multipartFile.getOriginalFilename();
						
						// Reject if hidden file or there is no name for it
						if(originalFilename == null || originalFilename.startsWith(".") || originalFilename.startsWith("~")) {
							errors.rejectValue(fieldName, CODE_FILE_ILLEGAL_FILE_NAME);
						}
						
						// Reject if non-supported extension
						String extension = FilenameUtils.getExtension(originalFilename);
						if(!IMG_EXTENSIONS.contains(extension.toLowerCase())) {
							errors.rejectValue(fieldName, CODE_FILE_WRONG_EXT, new Object[] {StringUtils.join(IMG_EXTENSIONS, ", ")}, null);
						}
						
						// Reject if size is too big
						Integer maxSize = moduleColumn.getSize();
						if(maxSize != null) {
							if(maxSize < multipartFile.getSize()) {
								errors.rejectValue(fieldName, CODE_FILE_TOO_BIG, new Object[] {maxSize}, null);
							}
						}
					}
					
					
					
				} else {
					
					String fieldName = getColumnValuesFieldName(columnId);
					
					// replace all manual line breaks from an HTML input (only for testing the blank value)
					String blankTestValue = columnType == ColumnType.HTML ? value.replaceAll("(<br\\s*/?>)", "") : value;
					
					if((StringUtils.isBlank(blankTestValue) && columnType != ColumnType.BOOL)) {
						// reject blank values if they are mandatory
						if(moduleColumn.getNotNull()) {
							errors.rejectValue(fieldName, CODE_REQUIRED);
						}
					} else {
						
						Object convertedValue;
						
						try {
							// do the type conversion
							convertedValue = columnTypeService.parseFromString(value, columnType);
						} catch (TypeConversionException e) {
							
							// Show appropriate error message if type conversion error occours
							String errorCode;
							
							switch (columnType) {
							case INT:
							case LONG:
							case DECIMAL:
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
						case HTML:
							
							Integer size = moduleColumn.getSize();
							
							// Check for too long strings
							if(size != null && size < ((String) convertedValue).length()) {
								errors.rejectValue(fieldName, CODE_TOO_LONG, new Object[] {size}, null);
								continue;
							}
							break;
						case ENUM:
							// Check for non-existing enum values
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
	
	/**
	 * Tells whether the module column should be processed by validation
	 * @param moduleColumn The column to check
	 * @return true if it should be processed, false if not
	 */
	protected abstract boolean isColumnProcessable(ModuleColumn moduleColumn);
	
	/**
	 * Calls {@link #getFieldName(String, Number)} for LOV values
	 */
	private String getLovValuesFieldName(Integer id) {
		return getFieldName("lovValues", id);
	}
	
	/**
	 * Calls {@link #getFieldName(String, Number)} for column values
	 */
	private String getColumnValuesFieldName(Long id) {
		return getFieldName("columnValues", id);
	}
	
	/**
	 * Calls {@link #getFieldName(String, Number)} for files
	 */
	private String getImagesFieldName(Long id) {
		return getFieldName("files", id);
	}
	
	/**
	 * Creates a field name that is submitted by Spring MVC form
	 * @param baseName The base name of the submitted field
	 * @param id The id of the submitted field
	 * @return The full field name
	 */
	private String getFieldName(String baseName, Number id) {
		StringBuilder sb = new StringBuilder();
		sb.append(baseName);
		sb.append("[");
		sb.append(id);
		sb.append("]");
		return sb.toString();
	}

}
