package com.example.Ecommerce.Controller;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Ecommerce.Exception.AddressNotFound;
import com.example.Ecommerce.Exception.UserNotFound;
import com.example.Ecommerce.Request.AddressRequestDto;
import com.example.Ecommerce.Response.AddressResponseDto;
import com.example.Ecommerce.Response.ApiResponse;
import com.example.Ecommerce.Response.PageResponse;
import com.example.Ecommerce.Service.AddressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/addresses")
public class AddressController {
     
	private final AddressService addressService;
	
	@PostMapping
    public ResponseEntity<ApiResponse> createAddress(@Valid @RequestBody AddressRequestDto addressRequestDto) {
        try {
            AddressResponseDto addressResponse = addressService.createAddress(addressRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Address created successfully", addressResponse));
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to create address: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getAddressById(@PathVariable Long id) {
        try {
            AddressResponseDto addressResponse = addressService.getAddressById(id);
            return ResponseEntity.ok(new ApiResponse("Address retrieved successfully", addressResponse));
        } catch (AddressNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to retrieve address: " + e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllAddress(
    		@RequestParam(defaultValue =  "0")int page,
    		@RequestParam(defaultValue = "10")int size,
    		@RequestParam(defaultValue = "id")String sortBy,
    		@RequestParam(defaultValue = "desc")String SortDir
    		
    		){
    	
    	try {
    		Sort.Direction diretion=SortDir.equalsIgnoreCase("desc")?Sort.Direction.DESC:Sort.Direction.ASC;
        	
        	Pageable pageable=PageRequest.of(page, size,Sort.by(diretion, sortBy));
        	
        	Page<AddressResponseDto> dto=addressService.getAllAddress(pageable);
        	
        	PageResponse<AddressResponseDto> saved =PageResponse.of(dto);
        	
        	
        	return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse("User orders fetched successfully", saved));
    	}catch (Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error occured", e.getMessage()));
		}
    	
    	
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getAddressesByUserId(@PathVariable Long userId) {
        try {
            List<AddressResponseDto> addresses = addressService.getAddressesByUserId(userId);
            return ResponseEntity.ok(new ApiResponse("User addresses retrieved successfully", addresses));
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to retrieve user addresses: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse> updateAddress(@PathVariable Long addressId, 
                                                   @RequestBody AddressRequestDto addressRequestDto) {
        try {
            AddressResponseDto addressResponse = addressService.updateAddress(addressId, addressRequestDto);
            return ResponseEntity.ok(new ApiResponse("Address updated successfully", addressResponse));
        } catch (AddressNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to update address: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse> deleteAddress(@PathVariable Long addressId) {
        try {
            addressService.deleteAddress(addressId);
            return ResponseEntity.ok(new ApiResponse("Address deleted successfully", null));
        } catch (AddressNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to delete address: " + e.getMessage(), null));
        }
    }

    @GetMapping("/user/{userId}/default")
    public ResponseEntity<ApiResponse> getDefaultAddressByUserId(@PathVariable Long userId) {
        try {
            AddressResponseDto addressResponse = addressService.getDefaultAddressByUserId(userId);
            return ResponseEntity.ok(new ApiResponse("Default address retrieved successfully", addressResponse));
        } catch (UserNotFound | AddressNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to retrieve default address: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{addressId}/default/user/{userId}")
    public ResponseEntity<ApiResponse> setDefaultAddress(@PathVariable Long addressId, 
                                                       @PathVariable Long userId) {
        try {
            AddressResponseDto addressResponse = addressService.setDefaultAddress(addressId, userId);
            return ResponseEntity.ok(new ApiResponse("Default address set successfully", addressResponse));
        } catch (UserNotFound | AddressNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to set default address: " + e.getMessage(), null));
        }
    }
}
