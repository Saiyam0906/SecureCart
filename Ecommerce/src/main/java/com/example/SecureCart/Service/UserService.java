package com.example.SecureCart.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.SecureCart.Exception.DuplicateUserException;
import com.example.SecureCart.Exception.InvalidPasswordException;
import com.example.SecureCart.Exception.UserNotFound;
import com.example.SecureCart.Interface.UserInterface;
import com.example.SecureCart.Mapper.UserMapper;
import com.example.SecureCart.Repository.UserRepository;
import com.example.SecureCart.Request.UserRequestDto;
import com.example.SecureCart.Request.UserUpdateDto;
import com.example.SecureCart.Request.changePasswordRequestDto;
import com.example.SecureCart.Response.ChangePasswordResponseDto;
import com.example.SecureCart.Response.UserResponseDto;
import com.example.SecureCart.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService implements UserInterface{
	
	private final PasswordService passowordService;
    
	private final UserRepository userRepository;
	
	private final UserMapper userMapper;
	
	
	
	@Override
	public UserResponseDto getUserById(Long id) {
		
		 log.debug("Fetching user with id: {}", id);
		 
		User user=userRepository.findById(id)
				.orElseThrow(()->new UserNotFound("User with id not exist" +id));
		
	     log.debug("Successfully found user with id: {}", id);
	     
		return userMapper.toResponseDto(user);
	}

	@Override
	public UserResponseDto getUserByUsername(String username) {
		
		 log.debug("Fetching user with username: {}", username);
		 
		User user=userRepository.findByFirstName(username)
				.orElseThrow(()->new UserNotFound("User with name does not exist"+username));
		
		log.debug("Successfully found user with username: {}", username);
		
		return userMapper.toResponseDto(user);
	}

	@Override
	@Transactional
	public UserResponseDto createUser(UserRequestDto userDto) {
	    
		 log.debug("Creating new user with email: {}", userDto.getEmail());
		 
		userRepository.findByFirstNameAndEmail(userDto.getFirstName(), userDto.getEmail())
		 .ifPresent(user->{
			 throw new DuplicateUserException("User with name and email already exist");
		 });
		
		User user=userMapper.toEntity(userDto);
		user.setPassword(passowordService.hashPassword(user.getPassword()));
		
		User savedUser = userRepository.save(user);
		
		 log.info("Successfully created user with id: {} and email: {}", 
				 savedUser.getId(),savedUser.getEmail());
		 
		return userMapper.toResponseDto(savedUser);
		
	}

	@Override
	@Transactional
	public void DeleteUser(Long id) {
		 log.debug("Deleting user with id: {}", id);
		 
		User user=userRepository.findById(id)
				.orElseThrow(()->new UserNotFound("User with id not exist" +id));
		
		userRepository.delete(user);
		
		log.info("User with id {} has been deleted", id);
		
	}

	@Override
	@Transactional
	public UserResponseDto updateUser(UserUpdateDto userDto, Long id) {
		
		log.debug("Updating user with id: {}", id);
        
		User user=userRepository.findById(id)
				.orElseThrow(()->new UserNotFound("User with id not exist" +id));
		
		userMapper.updateEntityFromDto(userDto, user);		
		
		
		
        User saved=userRepository.save(user);
		

        log.info("Successfully updated user with id: {}", id);
		return userMapper.toResponseDto(saved);
	}

	@Override
	public Page<UserResponseDto> getAllUsers(Pageable pageable) {
		log.debug("Fetching all users");
		
		Page<User> userPage=userRepository.findAll(pageable);
		
		log.debug("Found {} users out of {} total users", 
				userPage.getNumberOfElements(), userPage.getTotalElements());
		
		return userPage.map(userMapper::toResponseDto);
	}
	
	@Transactional
    public ChangePasswordResponseDto changePassword(Long userId, changePasswordRequestDto changePasswordRequest) {
        log.debug("Changing password for user with id: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFound("User not found"));
        
       
        if (!passowordService.verifyPassword(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Current password is incorrect");
        }
        
        
        passowordService.validatePasswordConfirmation(
            changePasswordRequest.getNewPassword(),
            changePasswordRequest.getConfirmPassword()
        );
        
       
        passowordService.validatePasswordChange(
            user.getPassword(),
            changePasswordRequest.getNewPassword()
        );
        
      
        user.setPassword(passowordService.hashPassword(changePasswordRequest.getNewPassword()));
        
        userRepository.save(user);
        
        log.info("Password successfully changed for user with id: {}", userId);
        
        return new ChangePasswordResponseDto(
            "Password changed successfully",
            LocalDateTime.now()
        );
        
	}

}
