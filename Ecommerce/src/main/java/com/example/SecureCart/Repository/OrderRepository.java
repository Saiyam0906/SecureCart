package com.example.SecureCart.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SecureCart.Request.OrderDto;
import com.example.SecureCart.enums.OrderStatus;
import com.example.SecureCart.model.Order;
import com.example.SecureCart.model.User;

public interface OrderRepository extends JpaRepository<Order, Long>{
    
	Page<Order> findByUser_Id(Long userId,Pageable pageable);
	
	Page<Order> findByOrderStatus(OrderStatus orderStatus,Pageable pageable);
	
}
