package com.example.SecureCart.Interface;

import org.springframework.web.multipart.MultipartFile;

import com.example.SecureCart.Request.ProfileUpdateDto;
import com.example.SecureCart.Response.ProfileResponseDto;

import jakarta.mail.Multipart;

public interface ProfileInterface {

	ProfileResponseDto getProfile(Long userId);
	ProfileResponseDto updateProfile(Long userId, ProfileUpdateDto requestDto);
	ProfileResponseDto uploadProfilePicture(Long userId,MultipartFile file);
	void deleteOldProfilePicture(String publicId);
	boolean isEmailVerified(Long userId);
	
}
