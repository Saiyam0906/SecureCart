package com.example.SecureCart.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SecureCart.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{
     
	Optional<Cart> findByUser_Id(Long userId);
}
