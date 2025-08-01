package com.example.SecureCart.Service;

import java.text.Normalizer.Form;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.SecureCart.Interface.EmailInterface;
import com.example.SecureCart.Repository.UserRepository;
import com.example.SecureCart.model.User;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements EmailInterface{
	
	private final JavaMailSender javaMailSender;
	
	private final UserRepository userRepository;

	 @Value("${spring.mail.username}")
	 private String fromEmail;
	 
	 @Value("${spring.token.expiry.hours}")
	 private int TOKEN_EXPIRY_HOURS;
	 
	    @Async
	    @Override
	    public void sendLoginFailureAlert(String email, int failedAttempts, int maxAttempts) {
	        String subject = "Security Alert: Failed Login Attempt";
	        String htmlContent = buildLoginFailureEmail(email, failedAttempts, maxAttempts);
	        sendEmail(email, subject, htmlContent, "Login failure alert");
	    }
	    
	    @Async
	    @Override
	    public void sendAccountLockedNotification(String email, long lockDurationMinutes) {
	        String subject = " Your Account Has Been Locked";
	        String htmlContent = buildAccountLockedEmail(email, lockDurationMinutes);
	        sendEmail(email, subject, htmlContent, "Account locked alert");
	    }
	    
	    @Async
	    @Override
	    public void sendPasswordChangeConfirmation(String email) {
	        String subject = "Password Change Confirmation";
	        String htmlContent = buildPasswordChangeEmail(email);
	        sendEmail(email, subject, htmlContent, "Password change confirmation");
	    }

	    @Async
	    @Override
	    public void sendOrderConfirmation(String email, String orderNumber, double totalAmount) {
	        String subject = "Order Confirmation";
	        String htmlContent = buildOrderConfirmationEmail(email, orderNumber, totalAmount);
	        sendEmail(email, subject, htmlContent, "Order confirmation");
	    }
	    
	    @Async
	    @Override
		public void sendEmailverification(String email) {
			User user=userRepository.findByEmail(email)
					.orElseThrow(()->new UsernameNotFoundException("User with eamil not found"+email));
			
	        if (user.isEmailVerified()) {
	            throw new RuntimeException("Email already verified");
	        }
	        
	        if (user.getVerificationToken() != null) {
	            user.setVerificationToken(null);
	            user.setTokenExpiry(null);
	        }
	        
	        String token=UUID.randomUUID().toString();
	        user.setVerificationToken(token);
	        user.setTokenExpiry(LocalDateTime.now().plusHours(TOKEN_EXPIRY_HOURS));
	        userRepository.save(user);
	        
	        String verificationLink = "http://localhost:8080/api/auth/verify-email?token=" + token;
	        String subject = "Verify Your Email Address";
	        String body=buildsendEmailverificationBody(verificationLink);
	        
	        sendEmail(email, subject, body,"verification Email");
	        
	        

			
		}
	    
	    @Async
	    @Override
	    public boolean verifyEmail(String token) {
	        User user = userRepository.findByVerificationToken(token)
	            .orElse(null);
	        
	        if (user == null) {
	            return false; 
	        }
	        
	        if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
	            return false; 
	        }
	        
	        
	        user.setEmailVerified(true);
	        user.setVerificationToken(null);
	        user.setTokenExpiry(null);
	        userRepository.save(user);
	        
	        return true;
	    }
	    
	    @Async
	    @Override
	    public boolean isEmailVerified(String email) {
	        User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	        return user.isEmailVerified();
	    }
	    
	    @Async
	    @Override
	    public void resendVerificationEmail(String email) {
	    	
	    	 User user = userRepository.findByEmail(email)
	    	            .orElseThrow(() -> new UsernameNotFoundException("User with email not found " + email));

	    	 if (user.isEmailVerified()) {
	    	        throw new RuntimeException("Email already verified");
	    	    }
	    	 
	    	 if (user.getTokenExpiry() != null && 
	    		        user.getTokenExpiry().isAfter(LocalDateTime.now().plusMinutes(2))) {
	    		        throw new RuntimeException("Please wait a few minutes before resending verification email.");
	    		    }
	    	 
	    	 
	    	 user.setVerificationToken(null);
	    	    user.setTokenExpiry(null);
	    	    userRepository.save(user);
	    	    
	    	 
	    	sendEmailverification(email); 
	    }
	    
	    
	    
	 private void sendEmail(String to, String subject, String htmlContent, String logContext) {
	        try {
	            MimeMessage message = javaMailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

	            helper.setTo(to);
	            helper.setFrom(fromEmail);
	            helper.setSubject(subject);
	            helper.setText(htmlContent, true);

	            javaMailSender.send(message);
	            log.info("{} sent to {}", logContext, to);

	        } catch (Exception e) {
	            log.error("Failed to send {} to {}", logContext, to, e);
	        }
	    }
	

	 private String buildsendEmailverificationBody(String verificationLink) {
		    return "<html>" +
		            "<body style='font-family: Arial, sans-serif; line-height:1.6;'>" +
		            "<h2>Email Verification</h2>" +
		            "<p>Thank you for registering! Please verify your email address by clicking the link below:</p>" +
		            "<p><a href='" + verificationLink + "' " +
		            "style='display:inline-block;padding:10px 20px;background-color:#4CAF50;color:white;text-decoration:none;border-radius:5px;'>" +
		            "Verify Email</a></p>" +
		            "<p>If the button above doesn’t work, copy and paste this link into your browser:</p>" +
		            "<p><a href='" + verificationLink + "'>" + verificationLink + "</a></p>" +
		            "<br/>" +
		            "<p><small>This link will expire in " + TOKEN_EXPIRY_HOURS + " hours.</small></p>" +
		            "</body>" +
		            "</html>";
		}

	
	
	private String buildLoginFailureEmail(String email, int failedAttempts, int maxAttempts) {
        return "<html><body style='font-family: Arial, sans-serif;'>" +
               "<h2 style='color:#721c24'> Security Alert</h2>" +
               "<p>We detected a failed login attempt for your account (<b>" + email + "</b>).</p>" +
               "<p><b>Failed Attempts:</b> " + failedAttempts + " of " + maxAttempts + "</p>" +
               "<p>If this wasn't you, please secure your account immediately.</p>" +
               "</body></html>";
    }

    private String buildAccountLockedEmail(String email, long lockDurationMinutes) {
        return "<html><body style='font-family: Arial, sans-serif;'>" +
               "<h2 style='color:#721c24'> Account Locked</h2>" +
               "<p>Your account (<b>" + email + "</b>) has been temporarily locked.</p>" +
               "<p><b>Lock Duration:</b> " + lockDurationMinutes + " minutes</p>" +
               "<p>Please try again later or contact support if needed.</p>" +
               "</body></html>";
    }

    private String buildPasswordChangeEmail(String email) {
        return "<html><body style='font-family: Arial, sans-serif;'>" +
               "<h2 style='color:#155724'> Password Changed</h2>" +
               "<p>Your password for (<b>" + email + "</b>) has been successfully changed.</p>" +
               "<p><b>Date:</b> " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "</p>" +
               "</body></html>";
    }

    private String buildOrderConfirmationEmail(String email, String orderNumber, double totalAmount) {
        return "<html><body style='font-family: Arial, sans-serif;'>" +
               "<h2 style='color:#155724'> Order Confirmed</h2>" +
               "<p>Order <b>" + orderNumber + "</b> placed successfully.</p>" +
               "<p><b>Total:</b> $" + String.format("%.2f", totalAmount) + "</p>" +
               "<p><b>Date:</b> " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "</p>" +
               "</body></html>";
    }

	
	
	
     
}
