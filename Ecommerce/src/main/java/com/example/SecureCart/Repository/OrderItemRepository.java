package com.example.SecureCart.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SecureCart.enums.OrderStatus;
import com.example.SecureCart.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

	List<OrderItem> findByOrderId(Long orderId);

	Page<OrderItem> findByProductId(Long productId, Pageable pageable);

	List<OrderItem> findByOrderOrderStatus(String orderStatus);

}
