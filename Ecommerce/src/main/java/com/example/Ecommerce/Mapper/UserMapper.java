package com.example.Ecommerce.Mapper;

import org.mapstruct.Mapper;

import com.example.Ecommerce.Request.UserRequestDto;
import com.example.Ecommerce.Response.UserResponseDto;
import com.example.Ecommerce.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
	User toEntity(UserRequestDto dto);
	
	UserResponseDto toRespnseDto(User user);

}
