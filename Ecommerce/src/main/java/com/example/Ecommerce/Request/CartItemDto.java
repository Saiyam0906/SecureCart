package com.example.Ecommerce.Request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemDto {
	private Long id;
	
	@Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
	
	 @NotNull(message = "Unit price is required")
	 @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
    private BigDecimal unitPrice;
	 
	  @NotNull(message = "Total price is required")
	    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be greater than 0")
    private BigDecimal totalPrice;
	  
	  @NotNull(message = "Product ID is required")
    private Long productId;
	  
	  @NotNull(message = "Cart ID is required")
    private Long cartId;
}
