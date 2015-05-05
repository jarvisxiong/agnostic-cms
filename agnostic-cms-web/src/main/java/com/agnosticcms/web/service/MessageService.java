package com.agnosticcms.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service for i18n messaging translations
 */
@Service
public class MessageService {

	@Autowired
	private MessageSource messageSource;
	
	/**
	 * Returns an i18n message string by its code
	 * @param code The code of i18n message
	 * @return The message string for the current locale
	 */
	public String getMessage(String code) {
		return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
	}
	
}
