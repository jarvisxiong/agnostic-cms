package com.agnosticcms.web.security;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Implementation of user details creation for Spring security
 */
public class ApacheUserDetailsService implements UserDetailsService {

	/**
	 * Creates Spring security's @UserDetails object for username retrieved from the
	 * session header
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = new User(username, "N/A", Collections.emptyList());
		return user;
	}

}
