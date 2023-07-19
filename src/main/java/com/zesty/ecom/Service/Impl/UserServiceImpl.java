package com.zesty.ecom.Service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zesty.ecom.Exception.DuplicateFieldExcepiton;
import com.zesty.ecom.Exception.ResourceNotFoundException;
import com.zesty.ecom.Mapper.UserMapper;
import com.zesty.ecom.Model.Role;
import com.zesty.ecom.Model.User;
import com.zesty.ecom.Payload.UserDto;
import com.zesty.ecom.Repository.RoleRepository;
import com.zesty.ecom.Repository.UserRepository;
import com.zesty.ecom.Service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserMapper userMapper;

	
	//create user
	@Override
	public UserDto createUser(UserDto userDto) {

		// checking if email exist or not
		String email = userDto.getEmail();
		if (userRepository.existsByEmail(email)) {
			throw new DuplicateFieldExcepiton("User", "Email", email);
		}
		System.out.println("user service " +userDto);
		User user = userMapper.mapToEntity(userDto);
		
		//setting roles
		Role role = this.roleRepository.findById(1).get();
		user.setRole(role);
		
		//encoding the password
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		
		User savedUser = userRepository.save(user);
		UserDto savedUserDto = userMapper.mapToDto(savedUser);
		return savedUserDto;
	}

	//get all users
	@Override
	public List<UserDto> getAllUsers() {
		List<User> users = userRepository.findAll();
		List<UserDto> usersDto = users.stream().map((u) -> userMapper.mapToDto(u)).collect(Collectors.toList());
		return usersDto;
	}

	//get user by id
	@Override
	public UserDto getUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", Long.toString(id)));
		return userMapper.mapToDto(user);
	}

	//update user
	@Override
	public UserDto updateUser(UserDto userDto, Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", Long.toString(id)));

		// checking if email id exist then throw error and
		// if email is equal to the updated email don't throw error
		String email = userDto.getEmail();
		if (userRepository.existsByEmail(email) && !email.trim().equals(user.getEmail())) {
			throw new DuplicateFieldExcepiton("User", "Email", email);
		}
		// updating user details
		user.setEmail(userDto.getEmail());
		user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setAddress(userDto.getAddress());
		user.setAbout(userDto.getAbout());
		user.setPhone(userDto.getPhone());
		// we're not getting time from user so will not set it
		user.setActive(userDto.isActive());
		User updatedUser = userRepository.save(user);
		return userMapper.mapToDto(updatedUser);
	}

	//delete user
	@Override
	public void deleteUser(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", Long.toString(id)));
		userRepository.delete(user);
	}
}
