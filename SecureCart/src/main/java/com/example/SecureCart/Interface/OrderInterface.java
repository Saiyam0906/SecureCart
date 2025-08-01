package com.example.SecureCart.Interface;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.SecureCart.Request.OrderDto;
import com.example.SecureCart.enums.OrderStatus;
import com.example.SecureCart.model.Order;

public interface OrderInterface {
	
	OrderDto placeOrder(Long userId);
	OrderDto getOrder(Long orderId);
	Page<OrderDto> getUserOrder(Long userId,Pageable pageable);
	OrderDto updateOrderStatus(Long orderId,OrderStatus orderstatus);
	void cancelOrder(Long orderId);
	Page<OrderDto> getOrderByStatus(OrderStatus orderstatus,Pageable pageable );
	
}
