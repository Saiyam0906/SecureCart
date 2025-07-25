package com.example.Ecommerce.Service.Address;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Ecommerce.Exception.AddressNotFound;
import com.example.Ecommerce.Exception.UserNotFound;
import com.example.Ecommerce.Mapper.AddressMapper;
import com.example.Ecommerce.Repository.AddressRepository;
import com.example.Ecommerce.Repository.UserRepository;
import com.example.Ecommerce.Request.AddressRequestDto;
import com.example.Ecommerce.Response.AddressResponseDto;
import com.example.Ecommerce.model.Address;
import com.example.Ecommerce.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService implements AddressInterface{
   
	private final AddressRepository addressRepository;
	
	private final AddressMapper addressMapper;
	
	private final UserRepository userRepository;
	
	
	@Override
	@Transactional
	public AddressResponseDto createAddress(AddressRequestDto addressRequestDto) {
		
		User user=userRepository.findById(addressRequestDto.getUserId())
				.orElseThrow(()->new UserNotFound("User with id not found"));
		
		Address address=addressMapper.toEntity(addressRequestDto);
		address.setUser(user);
		
		handleDefaultAddressLogic(address, addressRequestDto.isDefault(), user.getId());
        
		 
		 Address savedAddress = addressRepository.save(address);
		
		 return addressMapper.toResponseDto(savedAddress);
	}

	@Override
	public AddressResponseDto getAddressById(Long id) {
		Address address=addressRepository.findById(id)
				.orElseThrow(()-> new AddressNotFound("Address with id not found"+id));
		
		return addressMapper.toResponseDto(address);
	}

	@Override
	public Page<AddressResponseDto> getAllAddress(Pageable pageable) {
		
		Page<Address> address=addressRepository.findAll(pageable);
		return address.map(addressMapper::toResponseDto);
	}

	@Override
	public List<AddressResponseDto> getAddressesByUserId(Long userId) {
	
		User user=userRepository.findById(userId)
				.orElseThrow(()-> new UserNotFound("User with id not found"+userId));
		
		List<Address> address=addressRepository.findAllByUser(user);
		
		return address.stream()
				.map(addressMapper::toResponseDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public AddressResponseDto updateAddress(Long addressId, AddressRequestDto addressRequestDto) {
		
		Address address=addressRepository.findById(addressId)
				.orElseThrow(()-> new AddressNotFound("Address with id not found"));
		
		 if (addressRequestDto.getUserId() != null && 
		            !address.getUser().getId().equals(addressRequestDto.getUserId())) {
		            throw new IllegalArgumentException("Address does not belong to the specified user");
		        }
		 
		 handleDefaultAddressLogic(address, addressRequestDto.isDefault(), address.getUser().getId());
		 
		addressMapper.updateEntityFromDto(addressRequestDto, address);
		
		Address update=addressRepository.save(address);
		return addressMapper.toResponseDto(update);
	}

	@Override
	@Transactional
	public void deleteAddress(Long addressId) {
		Address address=addressRepository.findById(addressId)
				.orElseThrow(()-> new AddressNotFound("Address with id not found"+addressId));
		
		boolean wasDefault = address.isDefault();
		 Long userId = address.getUser().getId();
		 
		addressRepository.delete(address);
		
		if (wasDefault) {
            setAnotherAddressAsDefault(userId);
        }
		
	}

	@Override
	public AddressResponseDto getDefaultAddressByUserId(Long userId) {
		
		userRepository.findById(userId)
		.orElseThrow(() -> new UserNotFound("User with id " + userId + " not found"));
		
		Address address = addressRepository.findByUserIdAndIsDefaultTrue(userId)
		        .orElseThrow(() -> new AddressNotFound("Default address not found for user with id " + userId));
		    
		    return addressMapper.toResponseDto(address);
		
	}

	@Override
	@Transactional
	public AddressResponseDto setDefaultAddress(Long addressId, Long userId) {
		
		 User user = userRepository.findById(userId)
		            .orElseThrow(() -> new UserNotFound("User with id not found: " + userId));
		 
		 Address address = addressRepository.findById(addressId)
		            .orElseThrow(() -> new AddressNotFound("Address with id not found: " + addressId));
		 
		 if (!address.getUser().getId().equals(userId)) {
		        throw new IllegalArgumentException("Address does not belong to the user");
		    }
		 
		 if (address.isDefault()) {
	            return addressMapper.toResponseDto(address);
	        }
		 
		 int updatedCount = addressRepository.updateDefaultStatusByUserId(userId, false);
		 
		 
		    
		    address.setDefault(true);
		    Address updatedAddress =    addressRepository.save(address);
		    
		    
		return addressMapper.toResponseDto(updatedAddress);
	}
	
	  private void handleDefaultAddressLogic(Address address, boolean shouldBeDefault, Long userId) {
	        if (shouldBeDefault) {
	            
	            int updatedCount = addressRepository.updateDefaultStatusByUserId(userId, false);
	           
	            address.setDefault(true);
	        } else {
	            address.setDefault(false);
	            
	            
	            boolean hasDefaultAddress = addressRepository.existsByUserIdAndIsDefaultTrue(userId);
	            if (!hasDefaultAddress) {
	                address.setDefault(true);
	                
	            }
	        }
	    }
	  
	  private void setAnotherAddressAsDefault(Long userId) {
	        List<Address> userAddresses = addressRepository.findByUserId(userId);
	        if (!userAddresses.isEmpty()) {
	            Address firstAddress = userAddresses.get(0);
	            firstAddress.setDefault(true);
	            addressRepository.save(firstAddress);
	            
	        }
	    }

}
