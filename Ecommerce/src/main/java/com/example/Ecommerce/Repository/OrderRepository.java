package com.example.Ecommerce.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Ecommerce.Request.OrderDto;
import com.example.Ecommerce.enums.OrderStatus;
import com.example.Ecommerce.model.Order;
import com.example.Ecommerce.model.User;

public interface OrderRepository extends JpaRepository<Order, Long>{
    
	List<Order> findByUser_Id(Long userId);
	
	List<Order> findByOrderStatus(OrderStatus status);
	
}
