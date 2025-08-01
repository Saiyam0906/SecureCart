package com.example.Ecommerce.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.Ecommerce.Cloudinary.ClaudinaryResult;
import com.example.Ecommerce.Cloudinary.CloudinaryService;
import com.example.Ecommerce.Exception.AddressNotFound;
import com.example.Ecommerce.Exception.UserNotFound;
import com.example.Ecommerce.Interface.ProfileInterface;
import com.example.Ecommerce.Mapper.ProfileMapper;
import com.example.Ecommerce.Repository.AddressRepository;
import com.example.Ecommerce.Repository.UserRepository;
import com.example.Ecommerce.Request.ProfileUpdateDto;
import com.example.Ecommerce.Response.AddressResponseDto;
import com.example.Ecommerce.Response.ProfileResponseDto;
import com.example.Ecommerce.model.Address;
import com.example.Ecommerce.model.User;

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

	

}
