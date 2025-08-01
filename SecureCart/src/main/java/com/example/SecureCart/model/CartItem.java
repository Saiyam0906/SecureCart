package com.example.SecureCart.model;

import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Min(value = 1, message = "Quantity must be at least 1")
	private int quantity;
	
	@NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
	private BigDecimal unitPrice;
	
    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be greater than 0")
	private BigDecimal totalPrice;
	
	@ManyToOne
	@JoinColumn(name ="product_id")
	@NotNull(message = "Product is required")
	private Product product;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="cart_id")
	@NotNull(message = "Cart is required")
	private Cart cart;
	
	 public boolean isValidQuantity() {
	        return quantity > 0;
	    }
	 
	 public boolean isValidPrice() {
	        return unitPrice != null && unitPrice.compareTo(BigDecimal.ZERO) > 0;
	    }
	
	
	
}
