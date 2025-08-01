package com.example.SecureCart.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.SecureCart.Request.CartItemDto;
import com.example.SecureCart.Request.ProductDto;
import com.example.SecureCart.model.Cart;
import com.example.SecureCart.model.CartItem;
import com.example.SecureCart.model.Product;

import jakarta.transaction.Transactional;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{
	
	@Modifying
	@Transactional
	@Query("DELETE FROM CartItem c WHERE c.cart.id = :cartId")
	void deleteAllByCartId(Long id);
	
	 Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

}
