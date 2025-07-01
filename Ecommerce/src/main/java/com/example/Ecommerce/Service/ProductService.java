package com.example.Ecommerce.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.example.Ecommerce.Exception.ProductNotFoundException;
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
		//check if category exist in db
		// if yes, set it else save it as new category
		//then set as the new product category
		
		Category category= Optional.ofNullable(categoryRepository.findByName(productDto.getCategory().getName()))
				.orElseGet(()->{
					Category newCategory=new Category(productDto.getCategory().getName());
					return categoryRepository.save(newCategory);
				});
		
		Product productentity=productmapper.toEntity(productDto);
		productentity.setCategory(category);
		Product saveproduct=productRepository.save(productentity);
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
	public ProductDto updateProduct(ProductUpdateDto product, Long id) {
		Product updateproduct=productRepository.findById(id)
				.map(e-> updateExistingProduct(e, product))
				.map(productRepository::save)
				.orElseThrow(()-> new ProductNotFoundException("Product not found"));
		
		return productmapper.toDto(updateproduct);
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
	public List<ProductDto> getProductByCategory(String category) {
		List<Product> product=productRepository.findByCategoryName(category);
		return productmapper.toDtoList(product);
	}

	@Override
	public List<ProductDto> getProductByBrand(String brand) {
		List<Product> products = productRepository.findByBrandName(brand);
		return productmapper.toDtoList(products);
	}

	@Override
	public List<ProductDto> getProductByCategoryAndBrand(String category, String brand) {
		List<Product> products = productRepository.findByCategoryAndBrand(category,brand);
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
