package com.example.SecureCart.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SecureCart.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
      
	Optional<User> findByFirstName(String username);
	
	Optional<User> findByFirstNameAndEmail(String name, String email);

	Optional<User> findByEmail(String email);

	Optional<User> findByVerificationToken(String token);
}
