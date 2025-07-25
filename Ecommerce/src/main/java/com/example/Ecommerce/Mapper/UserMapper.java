package com.example.Ecommerce.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.Ecommerce.Request.UserRequestDto;
import com.example.Ecommerce.Response.UserResponseDto;
import com.example.Ecommerce.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
	User toEntity(UserRequestDto dto);
	
	UserResponseDto toResponseDto(User user);
	
	List<UserResponseDto> toDtoList(List<User> user);
	
	void  updateEntityFromDto(UserRequestDto dto,@MappingTarget User user);

}
