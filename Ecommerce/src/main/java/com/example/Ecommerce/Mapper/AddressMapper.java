package com.example.Ecommerce.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.example.Ecommerce.Request.AddressRequestDto;
import com.example.Ecommerce.Response.AddressResponseDto;
import com.example.Ecommerce.model.Address;
import com.example.Ecommerce.model.User;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    
	@Mapping(target = "user.id", source = "userId")
	Address toEntity(AddressRequestDto requestDto);
	
	
	@Mapping(target = "userName", source = "user.firstName")
	AddressResponseDto toResponseDto(Address address);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "user", source = "userId",qualifiedByName = "userIdToUser")
	 void updateEntityFromDto(AddressRequestDto requestDto, @MappingTarget Address address);
	
	
	@Named("userIdToUser")
	default User userIdToUser(Long userId) {
		if (userId == null) {
			return null;
		}
		User user = new User();
		user.setId(userId);
		return user;
	}
	    
}
