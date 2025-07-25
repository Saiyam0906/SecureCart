package com.example.Ecommerce.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Ecommerce.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
      
	Optional<User> findUserByName(String username);
	
	Optional<User> findByNameAndEmail(String name, String email);
}
