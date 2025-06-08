package com.example.Ecommerce.Service.Product;

import java.util.List;

import com.example.Ecommerce.Request.ProductDto;
import com.example.Ecommerce.model.Product;

public interface ProductServiceInterface {
	
	Product addProduct(ProductDto product);
	List<Product> getAllProduct();
	Product getProductById(Long id);
	void DelteProductById(Long id);
	void updateProduct(Product product,Long id);
	List<Product> getProductByCategory(String category);
	List<Product> getProductByBrand(String brand);
	List<Product> getProductByCategoryAndBrand(String category,String brand);
	List<Product> getProductByName(String name);
	List<Product> getProductByBrandAndName(String brand,String name);
	Long countProductByBrandAndName(String brand,String name);
	
	
	

}
