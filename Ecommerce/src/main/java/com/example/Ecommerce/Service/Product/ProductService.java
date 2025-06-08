package com.example.Ecommerce.Service.Product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Ecommerce.Exception.ProductNotFoundException;
import com.example.Ecommerce.Repository.ProductRepository;
import com.example.Ecommerce.Request.ProductDto;
import com.example.Ecommerce.model.Category;
import com.example.Ecommerce.model.Product;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService implements ProductServiceInterface{
	
	private final ProductRepository productRepository;	

	@Override
	public Product addProduct(ProductDto product) {
		//check if category exist in db
		// if yes, set it else save it as new product category
		
		
		return null;
	}
	
	private Product createProduct(ProductDto product,Category category) {
		return new Product(
				product.getName(),
				product.getBrand(),
				product.getDescription(),
				product.getPrice(),
				product.getInventory(),
				category
				);
				
	}

	@Override
	public List<Product> getAllProduct() {
		
		return productRepository.findAll();
	}

	@Override
	public Product getProductById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(()->new ProductNotFoundException("Product Not found"));
	}

	@Override
	public void DelteProductById(Long id) {
		 productRepository.findById(id)
				.ifPresentOrElse(productRepository::delete,
				       ()->{
				    	   throw new ProductNotFoundException("Product not found");
			             }
				   );
		
	}

	@Override
	public void updateProduct(Product product, Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Product> getProductByCategory(String category) {
		return productRepository.findByCategoryName(category);
	}

	@Override
	public List<Product> getProductByBrand(String brand) {
		return productRepository.findByBrandName(brand);
	}

	@Override
	public List<Product> getProductByCategoryAndBrand(String category, String brand) {
		return productRepository.findByCategoryAndBrand(category,brand);
	}

	@Override
	public List<Product> getProductByName(String name) {
		return productRepository.findByName(name);
	}

	@Override
	public List<Product> getProductByBrandAndName(String brand, String name) {
		return productRepository.findByBrandAndName(brand,name);
	}

	@Override
	public Long countProductByBrandAndName(String brand, String name) {
		return productRepository.countByBrandAndName(brand,name);
	}

}
