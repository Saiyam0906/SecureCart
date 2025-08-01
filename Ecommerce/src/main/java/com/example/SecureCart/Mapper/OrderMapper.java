package com.example.SecureCart.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.SecureCart.Request.OrderDto;
import com.example.SecureCart.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
	
	OrderDto toDTO(Order order);
	
	Order toEntity(OrderDto orderDTO);
	
	void updateOrderFromDTO(OrderDto orderDTO, @MappingTarget Order order);
	
	
	    List<OrderDto> toDTOList(List<Order> orders);
	    List<Order> toEntityList(List<OrderDto> orderDTOs);
    

}
