package com.example.SecureCart.Controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SecureCart.Request.CategoryDto;
import com.example.SecureCart.Request.CategoryUpdateDto;
import com.example.SecureCart.Response.ApiResponse;
import com.example.SecureCart.Service.CategoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${myapp.base.url}/images")
public class CategoryController {
	
	private final CategoryService categoryservice;
	
	@GetMapping("/Categories")
	public ResponseEntity<ApiResponse> getAllCategories(){
		try {
			
			List<CategoryDto> category=categoryservice.getAllCategory();
			return ResponseEntity.ok(new ApiResponse("Found", category));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Not Found", e.getMessage()));
			
		}
	}
	
	@PostMapping("/addCategory")
	public ResponseEntity<ApiResponse> addCategory(@RequestBody CategoryDto categoryDto){
		try {
			CategoryDto category = categoryservice.addCategory(categoryDto);
			 return ResponseEntity.status(HttpStatus.CREATED)
	                    .body(new ApiResponse("Category added successfully", category));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Error adding category", e.getMessage()));
		}
	}
	
	@GetMapping("/Category/{id}")
	public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
		try {
			CategoryDto category=categoryservice.getCategoryByid(id);
			return ResponseEntity.ok(new ApiResponse("Category Found", category));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Not Found", e.getMessage()));
		}
	}
	
	@GetMapping("/Name/{name}")
	public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
		try {
			CategoryDto category=categoryservice.getCategoryByName(name);
			return ResponseEntity.ok(new ApiResponse("Category Found", category));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Not Found", e.getMessage()));
		}
	}
	
	@GetMapping("/Categories/{id}")
	public ResponseEntity<ApiResponse> DeleteCategory(@PathVariable Long id){
		try {
			categoryservice.deleteCategoryByid(id);
			return ResponseEntity.ok(new ApiResponse("Category Deleted", null));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Not Deleted", e.getMessage()));
		}
	}
	
	@PutMapping("/UpdateCategory/{id}")
	public ResponseEntity<ApiResponse> UpdateCategory(@PathVariable Long id,@RequestBody CategoryUpdateDto categoryupdateDto){
		try {
			CategoryDto updateCategory=categoryservice.updateCategory(categoryupdateDto, id);
			return ResponseEntity.ok(new ApiResponse("Category Update", updateCategory));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Update Failed", e.getMessage()));
		}
	}
	
	
	
	

}
