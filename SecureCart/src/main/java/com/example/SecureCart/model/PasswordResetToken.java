package com.example.SecureCart.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PasswordResetToken {
   
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false, unique = true)
	    private String token;

	    @Column(nullable = false)
	    private LocalDateTime expiry;

	    @ManyToOne
	    @JoinColumn(nullable = false,name="user_id")
	    private User user;
	    
	    @Column(nullable = false)
	    private boolean used = false;
	    
	    @CreationTimestamp
	    @Column(nullable = false, updatable = false, name = "created_at")
	    private LocalDateTime createdAt;
	    
	    public PasswordResetToken(String token, User user, LocalDateTime expiry) {
	        this.token = token;
	        this.user = user;
	        this.expiry = expiry;
	        this.used = false;
	    }
	    
	    public boolean isExpired() {
	        return LocalDateTime.now().isAfter(this.expiry);
	    }
	    
	    public boolean isValid() {
	        return !isExpired() && !used;
	    }
	    
	    public void markAsUsed() {
	        this.used = true;
	    }
	    

}
