package com.example.SecureCart.Mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.SecureCart.Request.ProfileUpdateDto;
import com.example.SecureCart.Response.ProfileResponseDto;
import com.example.SecureCart.model.User;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
	
	 @Mapping(target = "defaultAddress", ignore = true) 
	 ProfileResponseDto toResponseDto(User user);
	 
	 @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	 void updateUserFromDto(ProfileUpdateDto dto, @MappingTarget User user);

}
