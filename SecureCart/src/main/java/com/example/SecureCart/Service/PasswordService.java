package com.example.SecureCart.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.SecureCart.Exception.InvalidPasswordException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordService {

	private final PasswordEncoder passwordEncoder;
	
	public String hashPassword(String plainPassword) {
        log.debug("Hashing password");
        String hashedPassword = passwordEncoder.encode(plainPassword);
        log.debug("Password successfully hashed");
        return hashedPassword;
    }
	
	public boolean verifyPassword(String plainPassword, String hashedPassword) {
        log.debug("Verifying password");
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }
	
	 public void validatePasswordChange(String currentHashedPassword, String newPlainPassword) {
	        if (verifyPassword(newPlainPassword, currentHashedPassword)) {
	            throw new InvalidPasswordException("New password must be different from current password");
	        }
	    }
	 
	 public void validatePasswordConfirmation(String password, String confirmPassword) {
	        if (!password.equals(confirmPassword)) {
	            throw new InvalidPasswordException("Password and confirm password do not match");
	        }
	    }
}
