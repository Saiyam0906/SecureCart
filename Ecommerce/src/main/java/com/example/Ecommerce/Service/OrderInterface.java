package com.example.Ecommerce.Service;

import java.util.List;

import com.example.Ecommerce.Request.OrderDto;
import com.example.Ecommerce.enums.OrderStatus;
import com.example.Ecommerce.model.Order;

public interface OrderInterface {
	
	OrderDto placeOrder(Long userId);
	OrderDto getOrder(Long orderId);
	List<OrderDto> getUserOrder(Long userId);
	OrderDto updateOrderStatus(Long orderId,OrderStatus orderstatus);
	void cancelOrder(Long orderId);
	List<OrderDto> getOrderByStatus(OrderStatus orderstatus);
	
}
