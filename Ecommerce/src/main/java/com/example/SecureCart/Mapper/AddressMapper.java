package com.example.SecureCart.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.example.SecureCart.Request.AddressRequestDto;
import com.example.SecureCart.Response.AddressResponseDto;
import com.example.SecureCart.model.Address;
import com.example.SecureCart.model.User;

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
