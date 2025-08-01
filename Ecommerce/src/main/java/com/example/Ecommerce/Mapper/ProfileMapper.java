package com.example.Ecommerce.Mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.Ecommerce.Request.ProfileUpdateDto;
import com.example.Ecommerce.Response.ProfileResponseDto;
import com.example.Ecommerce.model.User;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
	
	 @Mapping(target = "defaultAddress", ignore = true) 
	 ProfileResponseDto toResponseDto(User user);
	 
	 @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	 void updateUserFromDto(ProfileUpdateDto dto, @MappingTarget User user);

}
