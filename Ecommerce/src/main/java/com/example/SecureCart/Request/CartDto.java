package com.example.SecureCart.Request;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartDto {
	 private Long id;
	 
	 @NotNull(message = "Total amount is required")
	    @DecimalMin(value = "0.0", inclusive = true, message = "Total amount must be 0 or more")
	    private BigDecimal totalAmount;
	 
	 @NotNull(message = "Total items count is required")
	    private int totalItems;
	    private boolean empty;
	    @Valid
	    @NotNull(message = "Cart items list is required")
	    private List<@Valid CartItemDto> cartItems;

}
