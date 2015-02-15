package com.agnosticcms.web.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;

import com.agnosticcms.web.service.SessionService;

public class ApacheSessionAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

	@Autowired
	private SessionService sessionService;
	
	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		String principal = sessionService.getAttribute("cms-user", request);

        if (principal == null) {
            throw new PreAuthenticatedCredentialsNotFoundException("No apache credentials found");
        }

        return principal;
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		return "N/A";
	}

}
