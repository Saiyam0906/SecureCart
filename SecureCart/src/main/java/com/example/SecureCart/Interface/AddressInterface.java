package com.example.SecureCart.Interface;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.SecureCart.Request.AddressRequestDto;
import com.example.SecureCart.Response.AddressResponseDto;
import com.example.SecureCart.model.Address;

public interface AddressInterface {
       
   AddressResponseDto createAddress(AddressRequestDto addressRequestDto);
    
    
    AddressResponseDto getAddressById(Long id);
    
    
    Page<AddressResponseDto> getAllAddress(Pageable pageable);
    
    
    List<AddressResponseDto> getAddressesByUserId(Long userId);
    
    
    AddressResponseDto updateAddress(Long addressId, AddressRequestDto addressRequestDto);
    
    
    void deleteAddress(Long addressId);
    
    
    AddressResponseDto getDefaultAddressByUserId(Long userId);
    
    AddressResponseDto setDefaultAddress(Long addressId, Long userId);
	
 }
