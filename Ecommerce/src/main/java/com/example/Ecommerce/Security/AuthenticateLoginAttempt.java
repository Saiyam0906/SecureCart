package com.example.Ecommerce.Security;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.example.Ecommerce.Service.LoginAttemptService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticateLoginAttempt {
     
	private final LoginAttemptService loginAttemptService;
	
	@EventListener
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		String email=event.getAuthentication().getName();
		loginAttemptService.loginSucceeded(email);
		log.info("✅ Login succeeded for user: {}", email);
	}
	
	@EventListener  
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        String email = event.getAuthentication().getName();
        loginAttemptService.loginFailed(email);
        log.warn("Login failed for {}", email);
    }
}
