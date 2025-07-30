package com.example.Ecommerce.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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

import com.example.Ecommerce.Request.ProductDto;
import com.example.Ecommerce.Request.ProductUpdateDto;
import com.example.Ecommerce.Response.ApiResponse;
import com.example.Ecommerce.Service.ProductService;
import com.example.Ecommerce.model.Product;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${myapp.base.url}/Product")
public class ProductController{
	
	private final ProductService productservice;
	
	@GetMapping("/AllProducts")
	public ResponseEntity<ApiResponse> getAllProduct(){
		List<ProductDto> products = productservice.getAllProduct();
		return ResponseEntity.ok(new ApiResponse("Success", products));
	}
	
	@GetMapping("/product/{id}")
	public ResponseEntity<ApiResponse> getProductByID(@PathVariable  Long Id){
	   try{
        ProductDto product=productservice.getProductById(Id);
        return ResponseEntity.ok(new ApiResponse("Product fetched", product));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ApiResponse("Not find with id", e.getMessage()));
		}
	}
	
	@PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDto productDto){
    	try {
    	ProductDto savedProduct = productservice.addProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Product added successfully", savedProduct));
    }catch (Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Product didnt add", e.getMessage()));
	}
    	
    }
    
	@PutMapping("/updateProduct/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateDto productupdateDto,@PathVariable Long Id){
    	try {
    		ProductDto product= productservice.updateProduct(productupdateDto, Id);
    		 return ResponseEntity.ok(new ApiResponse("Product updated successfully", product));
    	}catch (Exception e) {
    		  return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    	                .body(new ApiResponse("Product update failed", e.getMessage()));
		}
    }
	
	@DeleteMapping("/deleteProduct/{productId}")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId){
		try {
		productservice.DelteProductById(productId);
		return ResponseEntity.ok(new ApiResponse("Delete Product success", productId));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ApiResponse("Product with id not found", productId));
		}
	}
	
	@GetMapping("/brand")
	public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brand) {
	    try {
	        List<ProductDto> products = productservice.getProductByBrand(brand);
	        if (products.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ApiResponse("No products found for brand: " + brand, null));
	        }
	        return ResponseEntity.ok(new ApiResponse("Products found", products));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
	            .body(new ApiResponse("Error retrieving products", e.getMessage()));
	    }
	}
	
	@GetMapping("/category")
	public ResponseEntity<ApiResponse> getProductByCategory(@RequestParam String category) {
	    try {
	        List<ProductDto> products = productservice.getProductByCategory(category);
	        if (products.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ApiResponse("No products found for category: " + category, null));
	        }
	        return ResponseEntity.ok(new ApiResponse("Products found", products));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
	            .body(new ApiResponse("Error retrieving products", e.getMessage()));
	    }
	}
	
	@GetMapping("/name")
	public ResponseEntity<ApiResponse> getProductByName(@RequestParam String name) {
	    try {
	        List<ProductDto> products = productservice.getProductByName(name);
	        if (products.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ApiResponse("No products found with name: " + name, null));
	        }
	        return ResponseEntity.ok(new ApiResponse("Products found", products));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
	            .body(new ApiResponse("Error retrieving products", e.getMessage()));
	    }
	}
	
	@GetMapping("/brand/product")
	public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName,@RequestParam String productName){
		try {
		List<ProductDto> products=productservice.getProductByBrandAndName(brandName, productName);
		
		 if (products.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(new ApiResponse("No products found for given name  and brand", null));
	        }
		return ResponseEntity.ok(new ApiResponse("Found", products));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ApiResponse("Not found", e.getMessage()));
		}
		
	}
	
	@GetMapping("/category/brand")
	public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String category,
	                                                                 @RequestParam String brand) {
	    try {
	        List<ProductDto> products = productservice.getProductByCategoryAndBrand(category, brand);
	        
	        if (products.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(new ApiResponse("No products found for given category and brand", null));
	        }
	        return ResponseEntity.ok(new ApiResponse("Products found", products));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
	                .body(new ApiResponse("Products not found", e.getMessage()));
	    }
	}
	
	@GetMapping("/product/count/by-brand/and-name")
	public ResponseEntity<ApiResponse> countProductByBrandAndName(@RequestParam String brand,@RequestParam String name){
		
		try {
	        Long count = productservice.countProductByBrandAndName(brand, name);

	        if (count == 0) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(new ApiResponse("No products found for given brand and name", count));
	        }

	        return ResponseEntity.ok(new ApiResponse("Product count retrieved successfully", count));
	        
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
	                .body(new ApiResponse("Error while counting products", e.getMessage()));
	    }
	}
	
	
	
	
	

}
