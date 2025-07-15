package com.example.Ecommerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Ecommerce.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
