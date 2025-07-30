package com.example.Ecommerce.Interface;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.Ecommerce.Request.UserRequestDto;
import com.example.Ecommerce.Request.UserUpdateDto;
import com.example.Ecommerce.Request.changePasswordRequestDto;
import com.example.Ecommerce.Response.ChangePasswordResponseDto;
import com.example.Ecommerce.Response.UserResponseDto;
import com.example.Ecommerce.model.User;

public interface UserInterface {
      
	UserResponseDto getUserById(Long id);
	UserResponseDto getUserByUsername(String username);
	UserResponseDto  createUser(UserRequestDto userDto);
	void DeleteUser(Long id);
	UserResponseDto updateUser(UserUpdateDto user,Long id);
	Page<UserResponseDto> getAllUsers(Pageable pageable);
	ChangePasswordResponseDto changePassword(Long userId, changePasswordRequestDto changePasswordRequest);
}
