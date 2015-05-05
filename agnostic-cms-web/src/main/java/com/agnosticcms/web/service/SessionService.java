package com.agnosticcms.web.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

/**
 * Service for operations with Apache session retrieved as HTTP header
 */
@Service
public class SessionService {

	/**
	 * Name of the session data incoming header
	 */
	private static final String APACHE_HTTP_SESSION_INPUT_HEADER = "X-Session";
	
	/**
	 * Name of the outgoind header for session data alteration
	 */
	private static final String APACHE_HTTP_SESSION_OUTPUT_HEADER = "X-Replace-Session";
	
	/**
	 * {@link #getAttribute(String, HttpServletRequest)}
	 */
	public String getAttribute(String key) {
		return getAttribute(key, getHttpServletRequest());
	}
	
	/**
	 * Gets single attribute from a session
	 * @param key Session attribute key
	 * @param request HttpServletRequest to retrieve session data from
	 * @return The attribute value
	 */
	public String getAttribute(String key, HttpServletRequest request) {
		String header = request.getHeader(APACHE_HTTP_SESSION_INPUT_HEADER);
		
		if(header == null) {
			return null;
		}
		
		Map<String, String> sessionMap = getSessionMap(header);
		
		return sessionMap.get(key);
		
	}
	
	/**
	 * Sets or changes session attribute's value
	 * @param key Key of the attribute
	 * @param value Value of the attribute
	 */
	public void setAttribute(String key, String value) {
		
		HttpServletResponse response = getHttpServletResponse();
		
		Map<String, String> sessionMap = getSessionMap(response.getHeader(APACHE_HTTP_SESSION_OUTPUT_HEADER));
		
		if(StringUtils.isEmpty(value)) {
			// setting empty value removes attribute from the session
			sessionMap.put(key, "");
		} else {
			sessionMap.put(key, value);
		}
		
		response.setHeader(APACHE_HTTP_SESSION_OUTPUT_HEADER, getSessionString(sessionMap));
		
	}
	
	/**
	 * Removes attribute from the session
	 * @param key The key of the attribute to remove
	 */
	public void removeAttribute(String key) {
		setAttribute(key, null);
	}
	
	/**
	 * Retrieves the session variables as a map
	 * @param sessionHeader The header to parse the values from
	 * @return Session variables in key => value format
	 */
	private Map<String, String> getSessionMap(String sessionHeader) {
		if(StringUtils.isEmpty(sessionHeader)) {
			return new HashMap<String, String>();
		}
		
		return Splitter.on("&").withKeyValueSeparator("=").split(sessionHeader);
	}
	
	/**
	 * Formats a map into Apache's session HTTP header format
	 * @param sessionMap The map to parse
	 * @return Session variables in Apache's session HTTP header format
	 */
	private String getSessionString(Map<String, String> sessionMap) {
		return Joiner.on("&").withKeyValueSeparator("=").join(sessionMap);
	}
	
	/**
	 * @return Current {@link HttpServletRequest}
	 */
	private HttpServletRequest getHttpServletRequest() {
		return getServletRequestAttributes().getRequest();
	}
	
	/**
	 * @return Current {@link HttpServletResponse}
	 */
	private HttpServletResponse getHttpServletResponse() {
		return getServletRequestAttributes().getResponse();
	}
	
	/**
	 * @return Current {@link ServletRequestAttributes}
	 */
	private ServletRequestAttributes getServletRequestAttributes() {
		return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	}
	
}
