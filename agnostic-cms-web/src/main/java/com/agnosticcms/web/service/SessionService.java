package com.agnosticcms.web.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.google.common.base.Splitter;

@Service
public class SessionService {

	private static final String APACHE_HTTP_SESSION_HEADER = "X-Session";
	
	public String getAttribute(String key, HttpServletRequest request) {
		String header = request.getHeader(APACHE_HTTP_SESSION_HEADER);
		
		if(header == null) {
			return null;
		}
		
		Map<String, String> sessionMap = getSessionMap(header);
		
		return sessionMap.get(key);
		
	}
	
	private Map<String, String> getSessionMap(String sessionHeader) {
		return Splitter.on("&").withKeyValueSeparator("=").split(sessionHeader);
	}
	
}
