package com.example.SecureCart.model;

import java.time.LocalDateTime;

import com.example.SecureCart.enums.EmailPriority;
import com.example.SecureCart.enums.EmailStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FailedEmail {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String recipientEmail;
    
    @Column(nullable = false)
    private String subject;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String htmlContent;
    
    @Column(nullable = false)
    private String logContext;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime lastRetryAt;
    
    @Column(nullable = false)
    private int retryCount = 0;
    
    @Column(nullable = false)
    private int maxRetries = 3;
    
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmailStatus status = EmailStatus.PENDING;
    
    @Column(nullable = false)
    private EmailPriority priority = EmailPriority.NORMAL;
    
    public FailedEmail(String recipientEmail, String subject, String htmlContent,
            String logContext, EmailPriority priority, String errorMessage) {
this.recipientEmail = recipientEmail;
this.subject = subject;
this.htmlContent = htmlContent;
this.logContext = logContext;
this.errorMessage = errorMessage;
this.priority = priority;
this.createdAt = LocalDateTime.now();
this.lastRetryAt = LocalDateTime.now();
this.status = EmailStatus.PENDING;
}
    
    public void incrementRetryCount() {
        this.retryCount++;
        this.lastRetryAt = LocalDateTime.now();
        
        if (this.retryCount >= this.maxRetries) {
            this.status = EmailStatus.FAILED;
        }
    }
    
    public boolean canRetry() {
        return this.retryCount < this.maxRetries && this.status == EmailStatus.PENDING;
    }
    
    public void markAsProcessed() {
        this.status = EmailStatus.SENT;
    }

}
