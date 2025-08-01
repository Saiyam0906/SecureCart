package com.example.SecureCart.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.SecureCart.Request.OrderItemDto;
import com.example.SecureCart.Response.ApiResponse;
import com.example.SecureCart.Response.PageResponse;
import com.example.SecureCart.Service.OrderItemService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class OrderItemController {
	
	private final OrderItemService orderItemService;
	
	@GetMapping("/orderItem/{orderId}")
	public ResponseEntity<ApiResponse> getOrderItemById(@PathVariable Long orderId){
		try {
			OrderItemDto order=orderItemService.getOrderItemById(orderId);
			return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Order with id found", order));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Order with id not found",e.getMessage()));
		}
		
	}
	
	 @GetMapping("/by-order/{orderId}")
	  public ResponseEntity<ApiResponse> getOrderItemsByOrderId(@PathVariable Long orderId) {
	        try {
	            List<OrderItemDto> items = orderItemService.getOrderItemsByOrderId(orderId);
	            return ResponseEntity.ok(new ApiResponse("Order items fetched", items));
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(new ApiResponse("No order items found", e.getMessage()));
	        }
	  }
	 
	 
	 
	 
	

}
