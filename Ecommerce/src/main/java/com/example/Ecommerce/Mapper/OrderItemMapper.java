package com.example.Ecommerce.Mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.Ecommerce.Request.OrderItemDto;
import com.example.Ecommerce.model.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
	
	
	@Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target  = "productName", source = "product.name")
    OrderItemDto toDTO(OrderItem orderItem);
	
	
	@Mapping(target = "order", ignore = true)
	@Mapping(target = "product", ignore = true)
	@Mapping(target="id",ignore = true)
	OrderItem toEntity(OrderItemDto orderItemDTO);
	 
	 
	 @Mapping(target = "order", ignore = true)
	 @Mapping(target = "product", ignore = true)
	 @Mapping(target = "id", ignore = true)
	 void updateOrderItemFromDTO(OrderItemDto orderItemDTO, @MappingTarget OrderItem orderItem);
	 
	 List<OrderItemDto> toDTOList(List<OrderItem> orderItems);
	 Set<OrderItemDto>toDTOSet(Set<OrderItem> orderItems);
	 
	 List<OrderItem> toEntityList(List<OrderItemDto> orderItemDTOs);
	 Set<OrderItem> toEntitySet(Set<OrderItemDto> orderItemDTOs);
	   
	   
	   
}
