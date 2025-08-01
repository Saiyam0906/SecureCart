package com.example.SecureCart.Interface;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.SecureCart.Request.UserRequestDto;
import com.example.SecureCart.Request.UserUpdateDto;
import com.example.SecureCart.Request.changePasswordRequestDto;
import com.example.SecureCart.Response.ChangePasswordResponseDto;
import com.example.SecureCart.Response.UserResponseDto;
import com.example.SecureCart.model.User;

public interface UserInterface {
      
	UserResponseDto getUserById(Long id);
	UserResponseDto getUserByUsername(String username);
	UserResponseDto  createUser(UserRequestDto userDto);
	void DeleteUser(Long id);
	UserResponseDto updateUser(UserUpdateDto user,Long id);
	Page<UserResponseDto> getAllUsers(Pageable pageable);
	ChangePasswordResponseDto changePassword(Long userId, changePasswordRequestDto changePasswordRequest);
}
