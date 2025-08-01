package com.example.SecureCart.Service;

import java.time.Duration;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.SecureCart.Interface.LoginAttemptInterface;
import com.example.SecureCart.Repository.UserRepository;
import com.example.SecureCart.model.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;




@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LoginAttemptService implements LoginAttemptInterface{
	
	
	@Value("${security.login.max-attempts:3}")
	private  int MAX_ATTEMPTS;
	
	@Value("${security.login.lock-duration:30}")
	private  int LOCK_DURATION_MINUTES;
	
	
	private final UserRepository userRepository;
	
	private final EmailService emailservice;


	@Override
	public void loginSucceeded(String email) {
		 try {
	            User user = userRepository.findByEmail(email)
	                    .orElse(null);
	            
	            if (user != null) {
	                user.setFailedAttempts(0);
	                user.setAccountLocked(false);
	                user.setLockTime(null);
	                userRepository.save(user);
	                log.info("Login succeeded for user: {}", email);
	            }
	        } catch (Exception e) {
	            log.error("Error processing successful login for email: {}", email, e);
	            throw new RuntimeException("Failed to process successful login", e);
	        }
		
	}


	@Override
	public void loginFailed(String email) {
		try {
			User user=userRepository.findByEmail(email)
				.orElse(null);
			
			if(user!=null) {
				int attempts=user.getFailedAttempts()+1;
				user.setFailedAttempts(attempts);
				
				if(attempts>=MAX_ATTEMPTS) {
					user.setAccountLocked(true);
					user.setLockTime(LocalDateTime.now());
					log.warn("Account locked for user: {} after {} failed attempts", email, attempts);
					
					 try {
	                        emailservice.sendAccountLockedNotification(email, LOCK_DURATION_MINUTES);
	                    } catch (Exception e) {
	                        log.error("Failed to send account lock notification to: {}", email, e);
	                        
	                    }
				}else {
					log.warn("Failed login attempt {} of {} for user: {}", attempts, MAX_ATTEMPTS, email);
					
					 try {
	                        emailservice.sendLoginFailureAlert(email, attempts, MAX_ATTEMPTS);
	                    } catch (Exception e) {
	                        log.error("Failed to send login failure alert to: {}", email, e);
	                       
	                    }
				}
				
				userRepository.save(user);
			}else {
                log.warn("Login failed for non-existent user: {}", email);
            }
		}catch (Exception e) {
			 log.error("Error processing failed login for email: {}", email, e);
	            throw new RuntimeException("Failed to process failed login", e);
		}
		
		
		
	}


	@Override
	public boolean isBlocked(String email) {
		User user=userRepository.findByEmail(email)
				.orElseThrow(()-> new UsernameNotFoundException("User not found wiht eamil"+email));
		
		if(user==null)return false;
		if(!user.isAccountLocked())return false;
		
		if(user.getLockTime()!=null) {
			LocalDateTime unlocktime=user.getLockTime().plusMinutes(LOCK_DURATION_MINUTES);
	         if(LocalDateTime.now().isAfter(unlocktime)) {
	        	 user.setAccountLocked(false);
	        	 user.setFailedAttempts(0);
	        	 user.setLockTime(null);
	        	 userRepository.save(user);
	        	 return false;
	         }
		}
		return true;
	}


	@Override
	public long getRemainingLockTime(String email) {
		
		try {
			User user=userRepository.findByEmail(email)
					.orElseThrow(()-> new UsernameNotFoundException("User not found wiht eamil"+email));
			 if (user == null || !user.isAccountLocked() || user.getLockTime() == null) {
		            return 0;
		        }
			 LocalDateTime unlockTime = user.getLockTime().plusMinutes(LOCK_DURATION_MINUTES);
		        return Duration.between(LocalDateTime.now(), unlockTime).toMinutes();
			
		}catch (Exception e) {
			log.error("Error getting remaining lock time for email: {}", email, e);
            return 0;
		}
		
		
	}
	
	
	
}
