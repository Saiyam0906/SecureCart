package com.example.Ecommerce.Service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.Ecommerce.Interface.LoginAttemptInterface;
import com.example.Ecommerce.Repository.UserRepository;
import com.example.Ecommerce.model.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginAttemptService implements LoginAttemptInterface{
    
	private static final int MAX_ATTEMPTS=3;
	private static final int LOCK_DURATION_MINUTES=30;
	
	
	private final UserRepository userRepository;


	@Override
	public void loginSucceeded(String email) {
		User user=userRepository.findByEmail(email)
				.orElseThrow(()->new UsernameNotFoundException("User not found wiht eamil"+email));
		if(user!=null) {
			user.setFailedAttempts(0);
            user.setAccountLocked(false);
            user.setLockTime(null);
            userRepository.save(user);
		}
		
	}


	@Override
	public void loginFailed(String email) {
		User user=userRepository.findByEmail(email)
				.orElseThrow(()-> new UsernameNotFoundException("User not found wiht eamil"+email));
		if(user!=null) {
			int attempts=user.getFailedAttempts()+1;
			user.setFailedAttempts(attempts);
			
			if(attempts>=MAX_ATTEMPTS) {
				user.setAccountLocked(true);
				user.setLockTime(LocalDateTime.now());
			}
			userRepository.save(user);
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
		User user=userRepository.findByEmail(email)
				.orElseThrow(()-> new UsernameNotFoundException("User not found wiht eamil"+email));
		 if (user == null || !user.isAccountLocked() || user.getLockTime() == null) {
	            return 0;
	        }
		 LocalDateTime unlockTime = user.getLockTime().plusMinutes(LOCK_DURATION_MINUTES);
	        return Duration.between(LocalDateTime.now(), unlockTime).toMinutes();
		
		
	}
	
	
	
}
