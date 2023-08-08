package com.zesty.ecom.Service;

import java.util.List;

import com.zesty.ecom.Payload.Dto.UserDto;

public interface UserService {
	
	//create user
	UserDto createUser(UserDto userDto);
	
	//get all users
	List<UserDto> getAllUsers();
	
	//get user by id
	UserDto getUserById(Long id);
	
	//get user by username
	UserDto getUserByUsername(String username);
	
	//update user
	UserDto updateUser(UserDto userDto,Long id);
	
	//delete user
	void deleteUser(Long id);
	
}
