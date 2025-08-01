package com.example.SecureCart.Service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.SecureCart.Exception.CartNotFoundException;
import com.example.SecureCart.Interface.CartInterface;
import com.example.SecureCart.Mapper.CartMapper;
import com.example.SecureCart.Repository.CartItemRepository;
import com.example.SecureCart.Repository.CartRepository;
import com.example.SecureCart.Request.CartDto;
import com.example.SecureCart.model.Cart;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements CartInterface{

	private final CartRepository cartrepository;
	
	private final CartItemRepository cartitemrepository;
	
	private final CartMapper cartmapper;
	
	@Override
	public CartDto getCart(Long userid) {
	Cart cart=cartrepository.findByUser_Id(userid)
			.orElseThrow(()-> new CartNotFoundException("Cart Not found"));
	
        cart.setTotalAmount(calculateTotalAmount(cart));
		return cartmapper.toDto(cart);
	}

	@Override
	public CartDto clearCart(Long userid) {
		Cart cart=cartrepository.findByUser_Id(userid)
				.orElseThrow(()-> new CartNotFoundException("Cart Not found"));
		
		cartitemrepository.deleteAllByCartId(userid);
		cart.setTotalAmount(calculateTotalAmount(cart));
		cartrepository.save(cart);
		
		return cartmapper.toDto(cart);
	}
	
	
	private BigDecimal calculateTotalAmount(Cart cart) {
	    return cart.getCartItems().stream()
	        .map(e -> e.getTotalPrice())
	        .reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	public BigDecimal getTotalPrice(Long userid) {
		Cart cart=cartrepository.findByUser_Id(userid)
				.orElseThrow(()->new CartNotFoundException("Cart Not found"));
		
		return calculateTotalAmount(cart);
		
		
	
	}

}
