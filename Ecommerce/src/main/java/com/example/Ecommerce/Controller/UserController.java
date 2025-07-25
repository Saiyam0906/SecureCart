package com.example.Ecommerce.Controller;

import java.util.List;

import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Ecommerce.Exception.UserNotFound;
import com.example.Ecommerce.Request.UserRequestDto;
import com.example.Ecommerce.Response.ApiResponse;
import com.example.Ecommerce.Response.PageResponse;
import com.example.Ecommerce.Response.UserResponseDto;
import com.example.Ecommerce.Service.UserService;
import com.example.Ecommerce.model.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserController {
	
	private final UserService userService;
	
	@PostMapping("/create-user")
	public ResponseEntity<ApiResponse> CreateUser(@RequestBody UserRequestDto userDto){
		
		try {
			UserResponseDto user=userService.createUser(userDto);
			return ResponseEntity.ok(new ApiResponse("User created Succesfully", user));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error occoured", e.getMessage()));
		}
		
	}
	
	@PutMapping("/Update-User/{id}")
	public ResponseEntity<ApiResponse> UpdateUser(@RequestBody UserRequestDto userDto,@PathVariable Long id){
		
		try {
			UserResponseDto user=userService.updateUser(userDto, id);
			return ResponseEntity.ok(new ApiResponse("User Updated Succesfully", user));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error occoured", e.getMessage()));
		}
	}
	
	@DeleteMapping("Delete-User/{id}")
	public ResponseEntity<ApiResponse> DeleteUser(@PathVariable Long id){
		
		try {
		userService.DeleteUser(id);
		return ResponseEntity.ok(new ApiResponse("User deleted successfully", null));
		}catch (UserNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ApiResponse("User not found", e.getMessage()));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to delete user", e.getMessage()));
		}
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id){
		try {
		UserResponseDto user=userService.getUserById(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("User found", user));
		}catch (UserNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ApiResponse("User not found", e.getMessage()));
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("User not found with id ", e.getMessage()));
		}
	}
	
	@GetMapping("/userByName/{username}")
	public ResponseEntity<ApiResponse> getUserByUsername(@PathVariable String username){
		 try {
		        UserResponseDto user = userService.getUserByUsername(username);
		        return ResponseEntity.ok(new ApiResponse("User found", user));
		    } catch (UserNotFound e) {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND)
		                .body(new ApiResponse("User not found", e.getMessage()));
		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body(new ApiResponse("Failed to fetch user", e.getMessage()));
		    }
	}
	
	@GetMapping("/users")
	public ResponseEntity<ApiResponse> getAllUsers(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id")String sortBy,
			@RequestParam(defaultValue="asc") String sortDir
			){
		try {
			 if (page < 0) {
		            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                    .body(new ApiResponse("Page number cannot be negative", null));
		        }
			
			 if (size <= 0 || size > 100) {
		            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                    .body(new ApiResponse("Page size must be between 1 and 100", null));
		        }
			Sort.Direction direction=sortDir.equalsIgnoreCase("desc")?
					Sort.Direction.DESC:Sort.Direction.ASC;
			
			Pageable pageable=PageRequest.of(page, size,Sort.by(direction,sortBy));
			
			Page<UserResponseDto> users=userService.getAllUsers(pageable);
			
			PageResponse<UserResponseDto> userPage=PageResponse.of(users);
			
			return ResponseEntity.ok(new ApiResponse("Users fetched successfully", userPage));
		}catch (Exception e) {
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body(new ApiResponse("Error occurred", e.getMessage()));
		}
				
		
		
	}
	

}
