package com.example.Ecommerce.Service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.Ecommerce.Exception.CartItemNotFoundException;
import com.example.Ecommerce.Exception.CartNotFoundException;
import com.example.Ecommerce.Exception.ProductNotFoundException;
import com.example.Ecommerce.Interface.CartItemInterface;
import com.example.Ecommerce.Mapper.CartItemMapper;
import com.example.Ecommerce.Repository.CartItemRepository;
import com.example.Ecommerce.Repository.CartRepository;
import com.example.Ecommerce.Repository.ProductRepository;
import com.example.Ecommerce.Request.CartDto;
import com.example.Ecommerce.Request.CartItemDto;
import com.example.Ecommerce.Request.ProductDto;
import com.example.Ecommerce.model.Cart;
import com.example.Ecommerce.model.CartItem;
import com.example.Ecommerce.model.Product;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CartItemService implements CartItemInterface{
	
	private final CartRepository cartRepository;
	
	private final CartItemRepository cartitemRepository;
	
	private final CartItemMapper cartitemMapper;
	
	private final ProductRepository productRepository;
	


	@Override
	public CartItemDto additem(CartItemDto cartItemDto) {
		
		Cart cart=cartRepository.findById(cartItemDto.getCartId())
				.orElseThrow(()-> new CartNotFoundException("Cart not found with id"));
		
		Product product=productRepository.findById(cartItemDto.getProductId())
				.orElseThrow(()->new ProductNotFoundException("Product with id not found"));
		
		Optional<CartItem> existingItem = cartitemRepository.findByCartIdAndProductId(
	            cartItemDto.getCartId(),
	            cartItemDto.getProductId()
	    );
		 
		 CartItem cartItem;
		 
		if(existingItem.isPresent()) {
			cartItem=existingItem.get();
			cartItem.setQuantity(cartItem.getQuantity()+cartItemDto.getQuantity());
		}else {
			cartItem = new CartItem();
	        cartItem.setCart(cart);
	        cartItem.setProduct(product);
	        cartItem.setQuantity(cartItemDto.getQuantity());
	        cartItem.setUnitPrice(product.getPrice());
		}
		cartItem.setTotalPrice(
                cartItem.getUnitPrice().multiply(new BigDecimal(cartItem.getQuantity()))
            );
		
		if (!cartItem.isValidQuantity()) {
	        throw new IllegalArgumentException("Invalid quantity: " + cartItem.getQuantity());
	    }
	    
	    if (!cartItem.isValidPrice()) {
	        throw new IllegalArgumentException("Invalid price: " + cartItem.getUnitPrice());
	    }
		 CartItem savedItem = cartitemRepository.save(cartItem);
		return cartitemMapper.toDto(savedItem);
		
	}
	

	@Override
	public void removeitem(Long cartItemId) {
		CartItem cartitem=cartitemRepository.findById(cartItemId)
				.orElseThrow(()->new CartItemNotFoundException("Cart item not found with id"));
		cartitemRepository.delete(cartitem);
	}
	

	@Override
	public CartItemDto updateItem(Long cartItemId, CartItemDto cartItemDto) {
		
		CartItem cartitem=cartitemRepository.findById(cartItemId)
				.orElseThrow(()-> new CartItemNotFoundException("Cart item not found with id"));
		
		if(cartItemDto.getCartId()!=null && !cartItemDto.getCartId().equals(cartitem.getCart().getId())) {
			Cart cart=cartRepository.findById(cartItemDto.getCartId())
					.orElseThrow(()-> new CartNotFoundException("Cart not found with id"));
			cartitem.setCart(cart);
		}
		
		if (cartItemDto.getProductId() != null && 
	            !cartItemDto.getProductId().equals(cartitem.getProduct().getId())) {
	            Product product = productRepository.findById(cartItemDto.getProductId())
	                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + cartItemDto.getProductId()));
	            cartitem.setProduct(product);
	        }
		
		cartitemMapper.updateEntityFromDto(cartItemDto, cartitem);
		
		if (!cartitem.isValidQuantity()) {
		    throw new IllegalArgumentException("Invalid quantity: " + cartitem.getQuantity());
		}

		if (!cartitem.isValidPrice()) {
		    throw new IllegalArgumentException("Invalid price: " + cartitem.getUnitPrice());
		}
		
		
		
		CartItem savedItem = cartitemRepository.save(cartitem);
		return cartitemMapper.toDto(savedItem);
		
		
	}
	

}
