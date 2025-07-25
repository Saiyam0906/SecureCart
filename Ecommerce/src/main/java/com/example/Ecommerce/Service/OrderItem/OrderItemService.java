package com.example.Ecommerce.Service.OrderItem;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Ecommerce.Exception.OrderItemNotFound;
import com.example.Ecommerce.Exception.UnauthorizedAccessException;
import com.example.Ecommerce.Mapper.OrderItemMapper;
import com.example.Ecommerce.Repository.OrderItemRepository;
import com.example.Ecommerce.Repository.OrderRepository;
import com.example.Ecommerce.Request.OrderDto;
import com.example.Ecommerce.Request.OrderItemDto;
import com.example.Ecommerce.enums.OrderStatus;
import com.example.Ecommerce.model.Order;
import com.example.Ecommerce.model.OrderItem;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderItemService implements OrderItemInterface{

	private final OrderItemRepository orderItemRepository;

	private final OrderItemMapper orderItemMapper;
	
	private final OrderRepository orderRepository;
	
	 @Override
	    public OrderItemDto getOrderItemById(Long id) {
	        OrderItem orderItem = orderItemRepository.findById(id)
	                .orElseThrow(() -> new OrderItemNotFound("Order item not found with id: " + id));
	        return orderItemMapper.toDTO(orderItem);
	    }
	 

	    @Override
	    public List<OrderItemDto> getOrderItemsByOrderId(Long orderId) {
	        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
	        if (items.isEmpty()) {
	            throw new OrderItemNotFound("No order items found for orderId: " + orderId);
	        }
	        return orderItemMapper.toDTOList(items);
	    }

	    @Override
	    public Page<OrderItemDto> getAllOrderItems(Pageable pageable) {
	        Page<OrderItem> orderItems = orderItemRepository.findAll(pageable);
	        return orderItems.map(orderItemMapper::toDTO);
	    }

	    @Override
	    public Page<OrderItemDto> getOrderItemsByProductId(Long productId, Pageable pageable) {
	        Page<OrderItem> orderItems = orderItemRepository.findByProductId(productId, pageable);
	        return orderItems.map(orderItemMapper::toDTO);
	    }

	    @Override
	    public BigDecimal calculateOrderItemsTotal(Long orderId) {
	        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
	        if (orderItems.isEmpty()) {
	            throw new OrderItemNotFound("No order items found for orderId: " + orderId);
	        }

	        return orderItems.stream()
	                .map(item -> {
	                	Integer quantity = item.getQuantity();
	                    if (item.getUnitPrice() == null || quantity == null) {
	                        throw new IllegalStateException("Invalid order item data: price or quantity is null");
	                    }
	                    return item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
	                })
	                .reduce(BigDecimal.ZERO, BigDecimal::add);
	    }

	    public List<OrderItemDto> getAllOrderItemsAsList() {
	        List<OrderItem> items = orderItemRepository.findAll();
	        return orderItemMapper.toDTOList(items);
	    }

	    public List<OrderItemDto> getOrderItemsByStatus(String status) {
	        List<OrderItem> items = orderItemRepository.findByOrderStatus(status);
	        return orderItemMapper.toDTOList(items);
	    }
	
	

	
	

}
