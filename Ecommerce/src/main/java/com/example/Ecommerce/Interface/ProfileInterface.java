package com.example.Ecommerce.Interface;

import org.springframework.web.multipart.MultipartFile;


import com.example.Ecommerce.Request.ProfileUpdateDto;
import com.example.Ecommerce.Response.ProfileResponseDto;

import jakarta.mail.Multipart;

public interface ProfileInterface {

	ProfileResponseDto getProfile(Long userId);
	ProfileResponseDto updateProfile(Long userId, ProfileUpdateDto requestDto);
	ProfileResponseDto uploadProfilePicture(Long userId,MultipartFile file);
	void deleteOldProfilePicture(String publicId);
	
}
