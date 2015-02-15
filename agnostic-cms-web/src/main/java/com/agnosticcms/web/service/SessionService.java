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

@Service
public class SessionService {

	private static final String APACHE_HTTP_SESSION_INPUT_HEADER = "X-Session";
	private static final String APACHE_HTTP_SESSION_OUTPUT_HEADER = "X-Replace-Session";
	
	public String getAttribute(String key) {
		return getAttribute(key, getHttpServletRequest());
	}
	
	public String getAttribute(String key, HttpServletRequest request) {
		String header = request.getHeader(APACHE_HTTP_SESSION_INPUT_HEADER);
		
		if(header == null) {
			return null;
		}
		
		Map<String, String> sessionMap = getSessionMap(header);
		
		return sessionMap.get(key);
		
	}
	
	public void setAttribute(String key, String value) {
		
		HttpServletResponse response = getHttpServletResponse();
		
		Map<String, String> sessionMap = getSessionMap(response.getHeader(APACHE_HTTP_SESSION_OUTPUT_HEADER));
		
		if(StringUtils.isEmpty(value)) {
			sessionMap.put(key, "");
		} else {
			sessionMap.put(key, value);
		}
		
		response.setHeader(APACHE_HTTP_SESSION_OUTPUT_HEADER, getSessionString(sessionMap));
		
	}
	
	public void removeAttribute(String key) {
		setAttribute(key, null);
	}
	
	private Map<String, String> getSessionMap(String sessionHeader) {
		if(StringUtils.isEmpty(sessionHeader)) {
			return new HashMap<String, String>();
		}
		
		return Splitter.on("&").withKeyValueSeparator("=").split(sessionHeader);
	}
	
	private String getSessionString(Map<String, String> sessionMap) {
		return Joiner.on("&").withKeyValueSeparator("=").join(sessionMap);
	}
	
	private HttpServletRequest getHttpServletRequest() {
		return getServletRequestAttributes().getRequest();
	}
	
	private HttpServletResponse getHttpServletResponse() {
		return getServletRequestAttributes().getResponse();
	}
	
	private ServletRequestAttributes getServletRequestAttributes() {
		return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	}
	
}
