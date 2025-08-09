package com.example.SecureCart.Service;

import java.text.Normalizer.Form;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.SecureCart.Exception.EmailSendException;
import com.example.SecureCart.Interface.EmailInterface;
import com.example.SecureCart.Repository.FailedEmailRepository;
import com.example.SecureCart.Repository.UserRepository;
import com.example.SecureCart.Request.EmailStatsDto;
import com.example.SecureCart.enums.EmailPriority;
import com.example.SecureCart.enums.EmailStatus;
import com.example.SecureCart.model.FailedEmail;
import com.example.SecureCart.model.User;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements EmailInterface{
	
	private final JavaMailSender javaMailSender;
	
	private final UserRepository userRepository;
	
	private final FailedEmailRepository failedEmailRepository;

	 @Value("${spring.mail.username}")
	 private String fromEmail;
	 
	 @Value("${spring.token.expiry.hours}")
	 private int TOKEN_EXPIRY_HOURS;
	 
	    @Async
	    @Override
	    public void sendLoginFailureAlert(String email, int failedAttempts, int maxAttempts) {
	        String subject = "Security Alert: Failed Login Attempt";
	        String htmlContent = buildLoginFailureEmail(email, failedAttempts, maxAttempts);
	        sendEmail(email, subject, htmlContent, "Login failure alert",EmailPriority.HIGH);
	    }
	    
	    @Async
	    @Override
	    public void sendAccountLockedNotification(String email, long lockDurationMinutes) {
	        String subject = " Your Account Has Been Locked";
	        String htmlContent = buildAccountLockedEmail(email, lockDurationMinutes);
	        sendEmail(email, subject, htmlContent, "Account locked alert",EmailPriority.HIGH);
	    }
	    
	    @Async
	    @Override
	    public void sendPasswordChangeConfirmation(String email) {
	        String subject = "Password Change Confirmation";
	        String htmlContent = buildPasswordChangeEmail(email);
	        sendEmail(email, subject, htmlContent, "Password change confirmation",EmailPriority.HIGH);
	    }

	    @Async
	    @Override
	    public void sendOrderConfirmation(String email, String orderNumber, double totalAmount) {
	        String subject = "Order Confirmation";
	        String htmlContent = buildOrderConfirmationEmail(email, orderNumber, totalAmount);
	        sendEmail(email, subject, htmlContent, "Order confirmation",EmailPriority.NORMAL);
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
	        
	        sendEmail(email, subject, body,"verification Email",EmailPriority.HIGH);
	        
	        

			
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
	    
	    
	 @CircuitBreaker(name= "emailService",fallbackMethod = "fallbackSendEmail")
	 @Retry(name="emailService")
	 private void sendEmail(String to, String subject, String htmlContent, String logContext, EmailPriority priority) {
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
	            throw new EmailSendException("Failed to send email to ");
	           
	        }
	    }
	 
	 public void sendDirectEmail(String to,String subject,String htmlContent) {
		 try {
			 MimeMessage message = javaMailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	            helper.setTo(to);
	            helper.setFrom(fromEmail);
	            helper.setSubject(subject);
	            helper.setText(htmlContent, true);
	            javaMailSender.send(message);
	            log.info("Admin alert email sent successfully");
		 }catch (Exception e) {
			 log.error("Failed to send admin alert email", e);
		}
		 
	 }
	 
	 public void fallbackSendEmail(String to, String subject, String htmlContent, 
             String logContext,EmailPriority priority ,Exception ex) {
		 
		 log.error("Circuit breaker activated. Email service is down. Failed to send {} to {}", logContext, to);
		 
		 
		 storeFailedEmailForRetry(to, subject, htmlContent, logContext, priority,ex.getMessage());
	 }
	 
	 @Transactional
	public void storeFailedEmailForRetry(String to, String subject, String htmlContent, 
            String logContext,EmailPriority priority,String ErrorMessage) {
		
		 try {
	            log.warn("Storing failed email for retry: {} to {}", logContext, to);
	            
	            FailedEmail failedEmail = new FailedEmail(to, subject, htmlContent, logContext, priority,ErrorMessage);
	            failedEmailRepository.save(failedEmail);
	            
	            log.info("Failed email stored successfully: {} to {}", logContext, to);
	            
	        }catch (Exception e) {
	        	log.error("Failed to store email for retry: {} to {}", logContext, to, e);
	         
	        	
		} 
		
	}
	 
	 @Scheduled(fixedRate = 300000)
	 @Transactional
	 public void retryfailedEmails() {
		 try {
			 log.info("Starting retry of failed emails");
			 
			 LocalDateTime cutoffTime=LocalDateTime.now().minusMinutes(5);
			 List<FailedEmail> failedEmails=failedEmailRepository.findRetryableEmailsOlderThan(cutoffTime);
			 
			  if (failedEmails.isEmpty()) {
	                log.debug("No failed emails to retry");
	                return;
	            }
			  
			  for(FailedEmail failedEmail : failedEmails) {
				  try {
					  log.info("Retrying failed email: {} to {}", failedEmail.getLogContext(), failedEmail.getRecipientEmail());
					  sendDirectEmail(failedEmail.getRecipientEmail(),failedEmail.getSubject(),failedEmail.getHtmlContent());
					  
					  failedEmail.markAsProcessed();
					  failedEmail.setLastRetryAt(LocalDateTime.now());
					  failedEmailRepository.save(failedEmail);
					  
					  log.info("Successfully retried failed email to {}", failedEmail.getRecipientEmail());
				  }catch (Exception e) {
						 log.warn("Retry failed for email to {}", failedEmail.getRecipientEmail(), e);
						 failedEmail.incrementRetryCount();
		                    failedEmail.setErrorMessage(e.getMessage());
		                    failedEmailRepository.save(failedEmail);
		                    
		                    if (!failedEmail.canRetry()) {
		                        log.error("Email permanently failed after {} retries: {} to {}", 
		                                failedEmail.getRetryCount(), failedEmail.getLogContext(), failedEmail.getRecipientEmail());
		                        failedEmail.setStatus(EmailStatus.FAILED);
		                        failedEmailRepository.save(failedEmail);
		                    }
						 
					}
			  }
				  
				  
			  }catch (Exception e) {
		            log.error("Error during failed email retry process", e);
		        }
			  
		 
	 }
	 
	 @Scheduled(cron = "0 0 2 * * ?")
	 public void cleanupoldEmails() {
		 try {
			 LocalDateTime cutoffDate = LocalDateTime.now().minusDays(7);
			 
			 int deletedSent = failedEmailRepository.deleteByStatusAndCreatedAtBefore(EmailStatus.SENT, cutoffDate);
			 int deletedFailed = failedEmailRepository.deleteByStatusAndCreatedAtBefore(EmailStatus.FAILED, cutoffDate);
			 
			 log.info("Cleaned up old processed emails older than {}", cutoffDate);
		 }catch (Exception e) {
			 log.error("Error during email cleanup", e);
			 
			}
	 }
	 
	 public EmailStatsDto getEmailStats() {
		 try {
			 long pendingCount = failedEmailRepository.countByStatus(EmailStatus.PENDING);
			 long failedCount = failedEmailRepository.countByStatus(EmailStatus.FAILED);
			 long sentCount = failedEmailRepository.countByStatus(EmailStatus.SENT);
			 
			 boolean healthy=true;
			 if(pendingCount>10) {
				 log.warn("Email service health degraded: {} pending failed emails", pendingCount);
				 healthy=false;
			 }
			 return new EmailStatsDto(pendingCount, failedCount, sentCount, healthy);
		 }catch (Exception e) {
			 log.error("Failed to get email service stats", e);
	            return new EmailStatsDto(0, 0, 0, false);
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
