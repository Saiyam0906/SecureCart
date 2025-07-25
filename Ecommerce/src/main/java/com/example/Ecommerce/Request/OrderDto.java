package com.example.Ecommerce.Request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import com.example.Ecommerce.enums.OrderStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    
	private Long id;

    @NotNull(message = "Order date is required")
    private LocalDate orderDate;

    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;

    @NotNull(message = "Order status is required")
    private OrderStatus orderStatus;

    private Set<OrderItemDto> orderItems;
}
