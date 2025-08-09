package com.example.SecureCart.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.SecureCart.enums.EmailStatus;
import com.example.SecureCart.model.FailedEmail;

@Repository
public interface FailedEmailRepository extends JpaRepository<FailedEmail, Long>{
	
	
	@Query("SELECT f FROM FailedEmail f WHERE f.status = 'PENDING' AND f.retryCount < f.maxRetries ORDER BY f.priority DESC, f.createdAt ASC")
    List<FailedEmail> findRetryableEmails();
   
	
	 @Query("SELECT f FROM FailedEmail f WHERE f.status = 'PENDING' AND f.retryCount < f.maxRetries AND f.lastRetryAt < :cutoffTime ORDER BY f.priority DESC, f.createdAt ASC")
	    List<FailedEmail> findRetryableEmailsOlderThan(@Param("cutoffTime") LocalDateTime cutoffTime);


	int deleteByStatusAndCreatedAtBefore(EmailStatus sent, LocalDateTime cutoffDate);


	long countByStatus(EmailStatus pending);

}
