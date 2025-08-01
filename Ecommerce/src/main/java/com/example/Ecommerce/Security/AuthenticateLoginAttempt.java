package com.example.Ecommerce.Security;

import java.time.LocalDateTime;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.example.Ecommerce.Repository.UserRepository;
import com.example.Ecommerce.Service.LoginAttemptService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticateLoginAttempt {
     
	private final LoginAttemptService loginAttemptService;
	private final UserRepository userRepository;
	
	
	@EventListener
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		String email=event.getAuthentication().getName();
		loginAttemptService.loginSucceeded(email);
		
		userRepository.findByEmail(email).ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            log.info("Last login updated for user: {}", email);
        });
		log.info("Login succeeded for user: {}", email);
	}
	
	@EventListener  
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        String email = event.getAuthentication().getName();
        loginAttemptService.loginFailed(email);
        log.warn("Login failed for {}", email);
    }
}
