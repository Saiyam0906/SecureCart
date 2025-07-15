package com.example.Ecommerce.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PutExchange;

import com.example.Ecommerce.Request.CartItemDto;
import com.example.Ecommerce.Response.ApiResponse;
import com.example.Ecommerce.cart.Service.CartItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart-item")
public class CartItemController {
     
	private final CartItemService cartitemservice;
	
	@PostMapping("/add-item")
	public ResponseEntity<ApiResponse> additem(@RequestBody CartItemDto cartitemDto){
		try {
			CartItemDto cart=cartitemservice.additem(cartitemDto);
			return ResponseEntity.ok(new ApiResponse("Item added to cart successfully", cart));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse("Failed to add item to cart", e.getMessage()));
		}
	}
	
	@DeleteMapping("/remove/{id}")
	public ResponseEntity<ApiResponse> removeitem(@PathVariable Long id){
		try {
			cartitemservice.removeitem(id);
			return ResponseEntity.ok(new ApiResponse("Item Deleted successfully", null));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse("Failed to Delete item", e.getMessage()));
		}
	}
	
	@PutMapping("/update-item/{id}")
	public ResponseEntity<ApiResponse> updateCartItem(@PathVariable Long id, @RequestBody CartItemDto cartItemDto) {
	    try {
	        CartItemDto updatedItem = cartitemservice.updateItem(id, cartItemDto);
	        return ResponseEntity.ok(new ApiResponse("Cart item updated successfully", updatedItem));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse("Failed to update item", e.getMessage()));
	    }
	}
	
	
}
