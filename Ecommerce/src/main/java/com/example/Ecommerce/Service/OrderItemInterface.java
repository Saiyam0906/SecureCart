package com.example.Ecommerce.Service;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.Ecommerce.Request.OrderItemDto;

public interface OrderItemInterface {

    OrderItemDto getOrderItemById(Long id);
    List<OrderItemDto> getOrderItemsByOrderId(Long orderId);
    Page<OrderItemDto> getAllOrderItems(Pageable pageable);
    Page<OrderItemDto> getOrderItemsByProductId(Long productId, Pageable pageable);
    BigDecimal calculateOrderItemsTotal(Long orderId);
}
