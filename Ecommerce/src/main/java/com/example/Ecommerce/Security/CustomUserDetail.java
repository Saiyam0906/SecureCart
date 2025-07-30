package com.example.Ecommerce.Security;

import java.util.stream.Collectors;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.Ecommerce.Repository.UserRepository;
import com.example.Ecommerce.Service.LoginAttemptService;
import com.example.Ecommerce.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetail implements UserDetailsService{
  
	private final UserRepository userRepository;
	private final LoginAttemptService loginAttemptService;
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	      User user=userRepository.findByEmail(email)
	    		  .orElseThrow(()-> new UsernameNotFoundException("User with email not found"));
	    		  
	      if(loginAttemptService.isBlocked(email)) {
	    	  long remaining =loginAttemptService.getRemainingLockTime(email);
	    	  throw new LockedException("Account is locked. Try again in " + remaining + " minutes.");
	      }
	      
	      return org.springframework.security.core.userdetails.User.builder()
	              .username(user.getEmail())
	              .password(user.getPassword()) 
	              .authorities(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
	              .build();
	}
	

}
