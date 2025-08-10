package com.example.SecureCart.Service;


import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.SecureCart.Interface.ForgotPasswordInterface;
import com.example.SecureCart.Repository.PasswordResetRepository;
import com.example.SecureCart.Repository.UserRepository;
import com.example.SecureCart.model.PasswordResetToken;
import com.example.SecureCart.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForgotPasswordService implements ForgotPasswordInterface{
	
	private final UserRepository userRepository;
	
	private final EmailService emailService;
	
	private final PasswordService passwordService;
	
	private final PasswordResetRepository passwordResetRepository;
	
	 private static final SecureRandom secureRandom = new SecureRandom();
	 
	 @Value("${spring.passwordReset.token.expiry}")
	 public  int  TOKEN_EXPIRY_HOURS;

	@Transactional
	@Override
	public void initiatePasswordReset(String email) {
		try {
			  log.info("Initiating password reset for email: {}", email);
			  
			  Optional<User> useropt =userRepository.findByEmail(email);
			  
			  if(useropt.isEmpty()) {
				  log.info("Password reset attempted for non-existent email: {}", email);
				  return;
			  }
			  
			  User user=useropt.get();
			  
			  passwordResetRepository.deleteByUser(user);
			  String generateToken = generateSecureToken();
			  LocalDateTime expiry=LocalDateTime.now().plusHours(TOKEN_EXPIRY_HOURS);
			  
			  PasswordResetToken passwordReset = new PasswordResetToken(generateToken, user, expiry);
			  passwordResetRepository.save(passwordReset);
			  
			  emailService.sendPasswordResetEmail(email, user.getFirstName(), generateToken);
			  
			  
		      log.info("Password reset token generated and email sent for: {}", email);
			   
		}catch (Exception e) {
			 log.error("Failed to initiate password reset for email: {}", email, e);
	            throw new RuntimeException("Failed to process password reset request");
		}
		
	}

	
	@Override
	public PasswordResetToken validateResetToken(String token) {
		if(token==null || token.trim().isEmpty()) {
			throw new RuntimeException("Reset token is required");
		}
		
		Optional<PasswordResetToken> tokenopt= passwordResetRepository.findByToken(token);
		
		if (tokenopt.isEmpty()) {
            throw new RuntimeException("Invalid reset token");
        }
		
		PasswordResetToken resetToken = tokenopt.get();
		
		  if (!resetToken.isValid()) {
	            
	            passwordResetRepository.delete(resetToken);
	            
	            if (resetToken.isExpired()) {
	                throw new RuntimeException("Reset token has expired");
	            } else {
	                throw new RuntimeException("Reset token has already been used");
	            }
	        }
	        
	        return resetToken;
	
	}

	@Override
	public void resetPassword(String token, String newPassword) {
		try {
			log.info("Processing password reset with token");
			
			PasswordResetToken resetToken = validateResetToken(token);  
			User user = resetToken.getUser();
			 user.setPassword(passwordService.hashPassword(newPassword));
			 
			 user.setFailedAttempts(0);
		     user.setAccountLocked(false);
		     user.setLockTime(null);
		     
		     
		     resetToken.markAsUsed();
		     passwordResetRepository.save(resetToken);
		     userRepository.save(user);
		     
		     emailService.sendPasswordChangeConfirmation(user.getEmail());
		        
		     log.info("Password reset completed successfully for user: {}", user.getEmail());
		}catch (Exception e) {
			log.error("Failed to reset password", e);
            throw new RuntimeException("Failed to reset password: " + e.getMessage());
		}
		
	}
	
	@Scheduled(fixedRate = 3600000)
	@Transactional
	public void cleanupExpiredTokens() {
		try {
			int deletedcount=passwordResetRepository.deleteByExpiryBefore(LocalDateTime.now());
			
			if (deletedcount > 0) {
                log.info("Cleaned up {} expired password reset tokens", deletedcount);
            }
		}catch (Exception e) {
			log.error("Error during password reset token cleanup", e);
		}
	}
	
	@Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void cleanupOldUsedTokens() {
        try {
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(7); 
            int deletedCount = passwordResetRepository.deleteUsedTokensOlderThan(cutoffDate);
            if (deletedCount > 0) {
                log.info("Cleaned up {} old used password reset tokens", deletedCount);
            }
        } catch (Exception e) {
            log.error("Error during old token cleanup", e);
        }
    }

	
	public String generateSecureToken() {
		byte[] random=new byte[32];
		secureRandom.nextBytes(random);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(random);
}


}
