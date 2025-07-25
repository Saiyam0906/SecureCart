package com.example.Ecommerce.Service.Order;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Ecommerce.Exception.CartNotFoundException;
import com.example.Ecommerce.Exception.OrderNotFound;
import com.example.Ecommerce.Exception.UserNotFound;
import com.example.Ecommerce.Mapper.OrderMapper;
import com.example.Ecommerce.Repository.CartRepository;
import com.example.Ecommerce.Repository.OrderItemRepository;
import com.example.Ecommerce.Repository.OrderRepository;
import com.example.Ecommerce.Repository.UserRepository;
import com.example.Ecommerce.Request.OrderDto;
import com.example.Ecommerce.enums.OrderStatus;
import com.example.Ecommerce.model.Cart;
import com.example.Ecommerce.model.Order;
import com.example.Ecommerce.model.OrderItem;
import com.example.Ecommerce.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService implements OrderInterface{
	
	private final OrderRepository orderRespository;
    
	private final CartRepository cartRepository;
	
	private final OrderMapper orderMapper;
	
	private final OrderItemRepository orderitemRepository;
	
	private final UserRepository userRepository;
	
	
	@Override
	@Transactional
	public OrderDto placeOrder(Long userId) {
		
		User user=userRepository.findById(userId)
				.orElseThrow(()-> new UserNotFound("User Not found with id"+ userId));
	       
		
		Cart cart=cartRepository.findByUser_Id(userId)
				.orElseThrow(()-> new CartNotFoundException("cart with userid not found" +userId));
				
		if (cart.getCartItems().isEmpty()) {
			throw new IllegalStateException("Cannot place order with empty cart");
		}
				
		Order order=Order.builder()
				.user(user)
				.orderDate(LocalDate.now())
				.orderStatus(OrderStatus.PLACED)
				.build();
		
		Order savedOrder=orderRespository.save(order);
		
		List<OrderItem> orderItems = createOrderItems(savedOrder, cart);
		
		List<OrderItem> savedOrderItems = orderitemRepository.saveAll  (orderItems);
		
		BigDecimal totalAmount=calculateTotalAmount(savedOrderItems);
		
		savedOrder.setTotalAmount(totalAmount);
		savedOrder.setOrderItems(new HashSet<>(savedOrderItems));
		
		Order finalOrder = orderRespository.save(savedOrder);
		
		cart.getCartItems().clear();
		cartRepository.save(cart);
		
		
		
		return orderMapper.toDTO(finalOrder);
	}

	@Override
	public OrderDto getOrder(Long orderId) {
		Order order=orderRespository.findById(orderId)
				.orElseThrow(()-> new OrderNotFound("Order Not found with id"+ orderId));
		return 	orderMapper.toDTO(order);
		
	}

	@Override
	public Page<OrderDto> getUserOrder(Long userId,Pageable pageable) {
		User user=userRepository.findById(userId)
				.orElseThrow(()-> new UserNotFound("User Not found with id"+ userId));
	    
		Page<Order> order=orderRespository.findByUser_Id(userId,pageable);
		
		return order.map(orderMapper::toDTO);
	}

	@Override
	@Transactional
	public OrderDto updateOrderStatus(Long orderId, OrderStatus orderstatus) {
		Order order=orderRespository.findById(orderId)
				.orElseThrow(()->new OrderNotFound("Order Not found with id "+orderId));
		
		if (order.getOrderStatus() == OrderStatus.DELIVERED) {
		    throw new IllegalStateException("Cannot update status. Order is already delivered.");
		}
		order.setOrderStatus(orderstatus);
		Order updatedOrder =	orderRespository.save(order);
		return orderMapper.toDTO(updatedOrder);
	}
	

	@Override
	@Transactional
	public void cancelOrder(Long orderId) {
		
		Order order=orderRespository.findById(orderId)
				.orElseThrow(()-> new OrderNotFound("Order with id not found"+orderId));
		
		if(order.getOrderStatus()==OrderStatus.DELIVERED) {
			throw new IllegalStateException("Cannot cancel order that is already shipped or delivered");
		}
		
		order.setOrderStatus(OrderStatus.CANCELLED);
		orderRespository.save(order);
	
		
	}

	@Override
	public Page<OrderDto> getOrderByStatus(OrderStatus orderstatus,Pageable pageable) {
		
		Page<Order> order=orderRespository.findByOrderStatus(orderstatus,pageable);
		return order.map(orderMapper::toDTO);
	}
	
	
	
	private List<OrderItem> createOrderItems(Order order,Cart cart){
		return cart.getCartItems().stream()
				.map(cartitem-> OrderItem.builder()
							.order(order)
							.product(cartitem.getProduct())
							.quantity(cartitem.getQuantity())
							.unitPrice(cartitem.getUnitPrice())
							.build())
				.toList();
	}
	
	private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
		return orderItemList.stream()
				.map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
		        .reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	
     
	
}
