package com.example.SecureCart.Controller;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SecureCart.Request.CartDto;
import com.example.SecureCart.Response.ApiResponse;
import com.example.SecureCart.Service.CartService;
import com.example.SecureCart.model.Cart;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class CartController {
     
	private final CartService cartservice;
	
	@GetMapping("/cart/{id}")
	private ResponseEntity<ApiResponse> getCart(@PathVariable Long id){
		try {
		CartDto cart=cartservice.getCart(id);
		return ResponseEntity.ok(new ApiResponse("Cart retrieved successfully", cart));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Cart with id not found", id));
		}
	}
	
	@GetMapping("/clear/{id}")
	private ResponseEntity<ApiResponse> clearcart(@PathVariable Long id){
           try {
        	   CartDto cart=cartservice.clearCart(id);
        	   return ResponseEntity.ok(new ApiResponse("Cart cleared successfully", cart));
           }catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Cart with id not found", id));
		}
	}
	
	@GetMapping("/total/{id}")
	private ResponseEntity<ApiResponse> getTotalPrice(@PathVariable Long id){
		try {
			 BigDecimal cart=cartservice.getTotalPrice(id);
      	   return ResponseEntity.ok(new ApiResponse("Total amount fetched successfully", cart));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Cart with id not found", id));
		}
		
	}
	
	
	
}
