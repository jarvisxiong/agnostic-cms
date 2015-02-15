package com.agnosticcms.web.security;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ApacheUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = new User(username, "N/A", Collections.emptyList());
		return user;
	}

}
