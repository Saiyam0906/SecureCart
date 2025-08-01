package com.example.SecureCart.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.SecureCart.Cloudinary.ClaudinaryResult;
import com.example.SecureCart.Cloudinary.CloudinaryService;
import com.example.SecureCart.Exception.AddressNotFound;
import com.example.SecureCart.Exception.UserNotFound;
import com.example.SecureCart.Interface.ProfileInterface;
import com.example.SecureCart.Mapper.ProfileMapper;
import com.example.SecureCart.Repository.AddressRepository;
import com.example.SecureCart.Repository.UserRepository;
import com.example.SecureCart.Request.ProfileUpdateDto;
import com.example.SecureCart.Response.AddressResponseDto;
import com.example.SecureCart.Response.ProfileResponseDto;
import com.example.SecureCart.model.Address;
import com.example.SecureCart.model.User;

import jakarta.mail.Multipart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService implements ProfileInterface{
	
	private final UserRepository userRepository;
	private final AddressService addressService;
	private final ProfileMapper profileMapper;
	private final EmailService emailService;
	private final CloudinaryService cloudinaryService;

	
	@Override
	public ProfileResponseDto getProfile(Long userId) {
		User user=userRepository.findById(userId)
				.orElseThrow(()->new UserNotFound("User with id not found"+userId));
		 ProfileResponseDto profile=profileMapper.toResponseDto(user);	
		
		 AddressResponseDto defaultAddress = addressService.getDefaultAddressByUserId(userId);
		    profile.setDefaultAddress(defaultAddress);
       
		return profile;
	}

	@Override
	public ProfileResponseDto updateProfile(Long userId, ProfileUpdateDto requestDto) {
		User user=userRepository.findById(userId)
				.orElseThrow(()->new UserNotFound("User with id Not found"+userId));
		
		if (!user.isEmailVerified()) {
            throw new RuntimeException("Please verify your email before updating profile.");
        }
		profileMapper.updateUserFromDto(requestDto, user);
		
		userRepository.save(user);
		
		ProfileResponseDto response = profileMapper.toResponseDto(user);
		
		AddressResponseDto defaultAddress = addressService.getDefaultAddressByUserId(userId);
	    response.setDefaultAddress(defaultAddress);

	    return response;
		
	}

	@Override
	public ProfileResponseDto uploadProfilePicture(Long userId, MultipartFile file) {
		try {
			User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFound("User with id not found: " + userId)); 
			
			 if (!user.isEmailVerified()) {
		            throw new RuntimeException("Please verify your email before uploading profile picture.");
		        }
			 
            if (user.getProfilePictureId() != null) {
                cloudinaryService.deleteImage(user.getProfilePictureId());
            }
           
            String folder = "profile_pictures/" + userId;
            String publicId = "profile_" + userId + "_" + System.currentTimeMillis();
            
            
            ClaudinaryResult uploadResult = cloudinaryService.uploadImage(file, folder, publicId);
            
          
            user.setProfilePictureUrl(uploadResult.getImageUrl());
            user.setProfilePictureId(uploadResult.getPublicId());
            user.setProfilePictureFormat(uploadResult.getFormat());
            user.setProfilePictureType(uploadResult.getResourceType());
            
            userRepository.save(user);
            
            log.info("Successfully updated profile picture for user {}: {}", 
                    userId, uploadResult.getPublicId());
            
          
            ProfileResponseDto response = profileMapper.toResponseDto(user);
            AddressResponseDto defaultAddress = addressService.getDefaultAddressByUserId(userId);
            response.setDefaultAddress(defaultAddress);
            
            return response;
		}catch (IOException e) {
	        log.error("Error uploading profile picture for user {}: {}", userId, e.getMessage());
	        throw new RuntimeException("Failed to upload profile picture: " + e.getMessage());

		}
		catch (Exception e) {
			 log.error("Unexpected error uploading profile picture for user {}: {}", userId, e.getMessage());
		        throw new RuntimeException("Failed to upload profile picture: " + e.getMessage());
		}
		
	}

	@Override
    public void deleteOldProfilePicture(String publicId) {
        cloudinaryService.deleteImage(publicId);
    }
	
	@Override
    public boolean isEmailVerified(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return user.isEmailVerified();
    }
	
	
	
	

	

}
