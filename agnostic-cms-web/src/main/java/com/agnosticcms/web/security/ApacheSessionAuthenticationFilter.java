package com.agnosticcms.web.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;

import com.agnosticcms.web.service.SessionService;

/**
 * Spring security filter that permits non-authenticated users from accessing Agnostic CMS core
 */
public class ApacheSessionAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

	@Autowired
	private SessionService sessionService;
	
	/**
	 * Checks that session header contains cms-user property. If it is so, user is considered logged-in
	 * @throws PreAuthenticatedCredentialsNotFoundException if no property is found
	 * @return Username of logged in user
	 */
	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		String principal = sessionService.getAttribute("cms-user", request);

        if (principal == null) {
            throw new PreAuthenticatedCredentialsNotFoundException("No apache credentials found");
        }

        return principal;
	}

	/**
	 * No credentials returned by this functionality
	 */
	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		return "N/A";
	}

}
