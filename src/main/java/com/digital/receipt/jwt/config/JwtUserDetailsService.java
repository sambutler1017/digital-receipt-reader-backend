package com.digital.receipt.jwt.config;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Used to add authenticationManagerBean for security.
 * 
 * @author Sam Butler
 * @since July 30, 2021
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String userName) {
		return new User("", "", new ArrayList<>());
	}
}
