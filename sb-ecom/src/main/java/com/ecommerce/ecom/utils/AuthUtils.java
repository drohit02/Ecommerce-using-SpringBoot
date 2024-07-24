package com.ecommerce.ecom.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ecommerce.ecom.model.User;
import com.ecommerce.ecom.repository.UserRepository;

@Component
public class AuthUtils {
	
	@Autowired
	private UserRepository userRepository;
	
	public String loggedInEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = this.userRepository.findByUsername(authentication.getName())
				.orElseThrow(()-> new UsernameNotFoundException("User not found!!!"));
		return user.getEmail(); 
	}
	
	public Long loggedInUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = this.userRepository.findByUsername(authentication.getName())
				.orElseThrow(()-> new UsernameNotFoundException("User not found!!!"));
		
		return user.getUserId();
	}
	
	public User loggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = this.userRepository.findByUsername(authentication.getName())
				.orElseThrow(()-> new UsernameNotFoundException("User not found!!!"));
		
		return user;
	}

}
