package com.example.Ecommerce.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Ecommerce.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{
     
	Optional<Cart> findByUser_Id(Long userId);
}
