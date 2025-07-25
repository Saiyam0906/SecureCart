package com.example.Ecommerce.Controller;

import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Ecommerce.Exception.CartNotFoundException;
import com.example.Ecommerce.Exception.OrderNotFound;
import com.example.Ecommerce.Exception.UserNotFound;
import com.example.Ecommerce.Request.OrderDto;
import com.example.Ecommerce.Response.ApiResponse;
import com.example.Ecommerce.Response.PageResponse;
import com.example.Ecommerce.Service.OrderService;
import com.example.Ecommerce.enums.OrderStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {
	
	private final OrderService orderService;
	
	@PostMapping("/PlaceOrder/{userId}")
	public ResponseEntity<ApiResponse> placeOrder(@PathVariable Long userId){
		try {
			OrderDto order=orderService.placeOrder(userId);
			return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Order placed for user ID", order));
			
		}catch (CartNotFoundException e) {
			
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Cart not found for user id: " + userId, null));
	        }
		catch (UserNotFound e) {
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User not found with id", userId));
			
		}catch (IllegalStateException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
	    }catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error occured", e.getMessage()));
		}
		
	}
	
	@GetMapping("/getOrder/{orderId}")
	public ResponseEntity<ApiResponse> getOrder(@PathVariable Long orderId){
		try {
			OrderDto order=orderService.getOrder(orderId);
			return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Order retrieved successfully", order));
		}catch (OrderNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Order not found with id" + orderId, null));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error occured", e.getMessage()));
		}
	}
	
	@GetMapping("/userOrder/{userId}")
	public ResponseEntity<ApiResponse> getUserOrder(
			@PathVariable Long userId,
			@RequestParam(defaultValue="0") int page,
			@RequestParam(defaultValue="10")int size,
			@RequestParam(defaultValue="orderStatus") String sortBy,
			@RequestParam(defaultValue = "desc")String sortDir
			){
		try {
			Sort.Direction direction=sortDir.equalsIgnoreCase("asc")? Sort.Direction.ASC:Sort.Direction.DESC;
			
			Pageable pageable=PageRequest.of(page, size,Sort.by(direction,sortBy));
			
			Page<OrderDto> pages=orderService.getUserOrder(userId, pageable);
			
			PageResponse<OrderDto> order=PageResponse.of(pages);
			
			 return ResponseEntity.status(HttpStatus.OK)
		                .body(new ApiResponse("User orders fetched successfully", order));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error occured", e.getMessage()));
		}
		
	}
	
	@PutMapping("/update-order/{orderId}/status")
	public ResponseEntity<ApiResponse> updateOrderStatus(@PathVariable Long orderId,@RequestParam OrderStatus orderstatus ){
		try {
			OrderDto order=orderService.updateOrderStatus(orderId, orderstatus);
			return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Order updated successfully", order));
			
		}catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Order is delivered", orderstatus));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error occured", e.getMessage()));
		}
		
	}
	
	@DeleteMapping("/Delete/{orderId}")
	public ResponseEntity<ApiResponse> CancleOrder(@PathVariable Long orderId){
		try {
			orderService.cancelOrder(orderId);
			return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Order cancled successfully", null));
		}catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Order is delivered", orderId));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error occured", e.getMessage()));
		}
	}
	
	@GetMapping("/orderByStatus/status")
	public ResponseEntity<ApiResponse> getOrderByStatus(
			@RequestParam OrderStatus orderStatus,
			@RequestParam(defaultValue = "0")int page,
			@RequestParam(defaultValue = "10")int size,
			@RequestParam(defaultValue = "orderStatus")String sortBy,
			@RequestParam(defaultValue = "Asc")String SortDir
			){
		
		try {
			Sort.Direction direction=SortDir.equalsIgnoreCase("asc")?Sort.Direction.ASC:Sort.Direction.DESC;
			
			Pageable pagebale=PageRequest.of(page, size,Sort.by(direction,sortBy));
			
			Page<OrderDto> pages = orderService.getOrderByStatus(orderStatus, pagebale);
			
			PageResponse<OrderDto>  savedPages =PageResponse.of(pages);
			
			return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Order by status", savedPages));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error occured", e.getMessage()));
		}
		
	}	
	
	
	
	
	
	

}
