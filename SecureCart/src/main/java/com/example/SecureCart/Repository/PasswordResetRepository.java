package com.example.SecureCart.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.SecureCart.model.PasswordResetToken;
import com.example.SecureCart.model.User;

public interface PasswordResetRepository extends JpaRepository<PasswordResetToken, Long>{

	
	void deleteByUser(User user);

	 Optional<PasswordResetToken> findByToken(String token);

	int deleteByExpiryBefore(LocalDateTime now);

	@Modifying
	@Query("DELETE FROM PasswordResetToken t WHERE t.used = true AND t.updatedAt < :cutoffDate")
	int deleteUsedTokensOlderThan(LocalDateTime cutoffDate); 

}
