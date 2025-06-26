package com.example.Ecommerce.Controller;

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
import org.springframework.web.service.annotation.PutExchange;

import com.example.Ecommerce.Request.CategoryDto;
import com.example.Ecommerce.Response.ApiResponse;
import com.example.Ecommerce.Service.CategoryService;
import com.example.Ecommerce.model.Category;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${myapp.base.url}/images")
public class CategoryController {
	
	private final CategoryService categoryservice;
	
	@GetMapping("/getCategories")
	private ResponseEntity<ApiResponse> getAllCategories(){
		try {
			
			List<Category> category=categoryservice.getAllCategory();
			return ResponseEntity.ok(new ApiResponse("Found", category));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Not Found", e.getMessage()));
			
		}
	}
	
	@PostMapping("/addCategory")
	private ResponseEntity<ApiResponse> addCategory(@RequestBody CategoryDto categoryDto){
		try {
			Category category = categoryservice.addCategory(categoryDto);
			return ResponseEntity.ok(new ApiResponse("Category added successfully", category));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Error adding category", e.getMessage()));
		}
	}
	
	@GetMapping("/getCategoryById/{id}")
	private ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
		try {
			Category category=categoryservice.getCategoryByid(id);
			return ResponseEntity.ok(new ApiResponse("Category Found", category));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Not Found", e.getMessage()));
		}
	}
	
	@GetMapping("/getCategoryByName/{name}")
	private ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
		try {
			Category category=categoryservice.getCategoryByName(name);
			return ResponseEntity.ok(new ApiResponse("Category Found", category));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Not Found", e.getMessage()));
		}
	}
	
	@GetMapping("/DeleteCategoty/{id}")
	private ResponseEntity<ApiResponse> DeleteCategory(@PathVariable Long id){
		try {
			categoryservice.deleteCategoryByid(id);
			return ResponseEntity.ok(new ApiResponse("Category Deleted", null));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Not Deleted", e.getMessage()));
		}
	}
	
	@PutMapping("/UpdateCategory/{id}")
	private ResponseEntity<ApiResponse> UpdateCategory(@PathVariable Long id,@RequestBody CategoryDto categoryDto){
		try {
			Category updateCategory=categoryservice.updateCategory(categoryDto, id);
			return ResponseEntity.ok(new ApiResponse("Category Update", updateCategory));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Update Failed", e.getMessage()));
		}
	}
	
	
	
	

}
