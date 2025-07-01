package com.example.Ecommerce.Service;

import java.util.List;

import com.example.Ecommerce.Request.ProductDto;
import com.example.Ecommerce.Request.ProductUpdateDto;
import com.example.Ecommerce.model.Product;

public interface ProductServiceInterface {
	
	ProductDto addProduct(ProductDto productDto);
	List<ProductDto> getAllProduct();
	ProductDto getProductById(Long id);
	void DelteProductById(Long id);
	ProductDto updateProduct(ProductUpdateDto product,Long id);
	List<ProductDto> getProductByCategory(String category);
	List<ProductDto> getProductByBrand(String brand);
	List<ProductDto> getProductByCategoryAndBrand(String category,String brand);
	List<ProductDto> getProductByName(String name);
	List<ProductDto> getProductByBrandAndName(String brand,String name);
	Long countProductByBrandAndName(String brand,String name);
	
	
	

}
