package com.example.Ecommerce.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.Ecommerce.Request.AddressRequestDto;
import com.example.Ecommerce.Response.AddressResponseDto;
import com.example.Ecommerce.model.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    
	
	Address toEntity(AddressRequestDto requestDto);
	
	
	
	AddressResponseDto toResponseDto(Address address);
	
	
	 void updateEntityFromDto(AddressRequestDto requestDto, @MappingTarget Address address);
	    
}
