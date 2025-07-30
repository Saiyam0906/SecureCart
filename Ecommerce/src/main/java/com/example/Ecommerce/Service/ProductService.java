package com.example.Ecommerce.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.Ecommerce.Exception.ProductNotFoundException;
import com.example.Ecommerce.Interface.ProductServiceInterface;
import com.example.Ecommerce.Mapper.ProductMapper;
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
	
	private final ProductMapper productmapper;

	@Override
	public ProductDto addProduct(ProductDto productDto) {
		// Handle category creation if needed
		if (productDto.getCategory() != null && productDto.getCategory().getName() != null) {
			Category category = Optional.ofNullable(categoryRepository.findByName(productDto.getCategory().getName()))
					.orElseGet(() -> {
						Category newCategory = new Category(productDto.getCategory().getName());
						return categoryRepository.save(newCategory);
					});
			// Update the DTO with the saved category (including ID)
			productDto.getCategory().setId(category.getId());
		}
		
		// Let MapStruct handle the full mapping including category
		Product productentity = productmapper.toEntity(productDto);
		Product saveproduct = productRepository.save(productentity);
		return productmapper.toDto(saveproduct);
	}
	

	@Override
	public List<ProductDto> getAllProduct() {
		List<Product> products=productRepository.findAll();
		return productmapper.toDtoList(products);
	}

	@Override
	public ProductDto getProductById(Long id) {
		Product product=productRepository.findById(id)
				.orElseThrow(()-> new ProductNotFoundException("Product Not found"));
		return productmapper.toDto(product);
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
	public ProductDto updateProduct(ProductUpdateDto productUpdateDto, Long id) {
		// Step 1: Find the existing product
		Product existingProduct = productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product not found"));
		
		// Step 2: Manually update each field
		existingProduct.setName(productUpdateDto.getName());
		existingProduct.setBrand(productUpdateDto.getBrand());
		existingProduct.setPrice(productUpdateDto.getPrice());
		existingProduct.setInventory(productUpdateDto.getInventory());
		existingProduct.setDescription(productUpdateDto.getDescription());
		
		// Step 3: Handle category update
		if (productUpdateDto.getCategory() != null && productUpdateDto.getCategory().getName() != null) {
			Category category = Optional.ofNullable(categoryRepository.findByName(productUpdateDto.getCategory().getName()))
					.orElseGet(() -> {
						Category newCategory = new Category(productUpdateDto.getCategory().getName());
						return categoryRepository.save(newCategory);
					});
			existingProduct.setCategory(category);
		}
		
		// Step 4: Save and return
		Product updatedProduct = productRepository.save(existingProduct);
		return productmapper.toDto(updatedProduct);
	}
	
	

	@Override
	public List<ProductDto> getProductByCategory(String category) {
		List<Product> product=productRepository.findByCategoryName(category);
		return productmapper.toDtoList(product);
	}

	@Override
	public List<ProductDto> getProductByBrand(String brand) {
		List<Product> products = productRepository.findByBrand(brand);
		return productmapper.toDtoList(products);
	}

	@Override
	public List<ProductDto> getProductByCategoryAndBrand(String category, String brand) {
		List<Product> products = productRepository.findByCategory_NameAndBrand(category,brand);
		return productmapper.toDtoList(products);
	}

	@Override
	public List<ProductDto> getProductByName(String name) {
		List<Product> products = productRepository.findByName(name);
		return productmapper.toDtoList(products);
	}

	@Override
	public List<ProductDto> getProductByBrandAndName(String brand, String name) {
		List<Product> products = productRepository.findByBrandAndName(brand,name);
		return productmapper.toDtoList(products);
	}

	@Override
	public Long countProductByBrandAndName(String brand, String name) {
		return productRepository.countByBrandAndName(brand,name);
	}

}
