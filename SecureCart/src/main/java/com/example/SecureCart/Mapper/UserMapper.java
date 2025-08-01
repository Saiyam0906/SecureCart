package com.example.SecureCart.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.SecureCart.Request.UserRequestDto;
import com.example.SecureCart.Request.UserUpdateDto;
import com.example.SecureCart.Response.UserResponseDto;
import com.example.SecureCart.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
	User toEntity(UserRequestDto dto);
	
	UserResponseDto toResponseDto(User user);
	
	List<UserResponseDto> toDtoList(List<User> user);
	
	void  updateEntityFromDto(UserUpdateDto dto,@MappingTarget User user);

}
