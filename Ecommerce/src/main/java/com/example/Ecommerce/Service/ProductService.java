package com.example.Ecommerce.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.example.Ecommerce.Exception.ProductNotFoundException;
import com.example.Ecommerce.Repository.CategoryRepository;
import com.example.Ecommerce.Repository.ProductRepository;
import com.example.Ecommerce.Request.ProductDto;
import com.example.Ecommerce.Request.ProductUpdateDto;
import com.example.Ecommerce.model.Category;
import com.example.Ecommerce.model.Product;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService implements ProductServiceInterface{
	
	private final ProductRepository productRepository;	
	
	private final CategoryRepository categoryRepository;

	@Override
	public Product addProduct(ProductDto product) {
		//check if category exist in db
		// if yes, set it else save it as new category
		//then set as the new product category
		
		Category category= Optional.ofNullable(categoryRepository.findByName(product.getCategory().getName()))
				.orElseGet(()->{
					Category newCategory=new Category(product.getCategory().getName());
					return categoryRepository.save(newCategory);
				});
		product.setCategory(category);
				

		
		
		return productRepository.save(createProduct(product,category));
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
	public Product updateProduct(ProductUpdateDto product, Long id) {
		return productRepository.findById(id)
				.map(existingProduct->updateExistingProduct(existingProduct, product))
				.map(productRepository::save)
				.orElseThrow(()-> new ProductNotFoundException("Product not found"));
		
	}
	
	private Product updateExistingProduct(Product existingProduct,ProductUpdateDto product) {
	    existingProduct.setName(product.getName());
	    existingProduct.setBrand(product.getBrand());
	    existingProduct.setPrice(product.getPrice());
	    existingProduct.setInventory(product.getInventory());
	    existingProduct.setDescription(product.getDescription());
	    
	    Category category =  categoryRepository.findByName(product.getCategory().name());
	    existingProduct.setCategory(category);
	    return existingProduct;
		
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
