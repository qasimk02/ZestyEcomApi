package com.zesty.ecom.Service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zesty.ecom.Exception.DuplicateFieldExcepiton;
import com.zesty.ecom.Exception.ResourceNotFoundException;
import com.zesty.ecom.Mapper.UserMapper;
import com.zesty.ecom.Model.Cart;
import com.zesty.ecom.Model.Role;
import com.zesty.ecom.Model.User;
import com.zesty.ecom.Payload.Dto.UserDto;
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

	// create user
	@Override
	public UserDto createUser(UserDto userDto) {

		// checking if email exist or not
		String email = userDto.getEmail();
		if (userRepository.existsByEmail(email)) {
			throw new DuplicateFieldExcepiton("User", "Email", email);
		}
		User user = userMapper.mapToEntity(userDto);

		// setting roles
		Role role = this.roleRepository.findById(1).orElseThrow(() -> new ResourceNotFoundException("Role", "Id", "1"));
		user.setRole(role);
		user.setActive(true);

		// encoding the password
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));

		// creating cart when user register
		Cart cart = new Cart();
		cart.setTotalPrice(0.0);
		cart.setTotalDiscountedPrice(0.0);
		cart.setUser(user);
		user.setCart(cart);

		User savedUser = userRepository.save(user);

		UserDto savedUserDto = userMapper.mapToDto(savedUser);
		return savedUserDto;
	}

	// get all users
	@Override
	public List<UserDto> getAllUsers() {
		List<User> users = userRepository.findAll();
		List<UserDto> usersDto = users.stream().map((u) -> userMapper.mapToDto(u)).collect(Collectors.toList());
		return usersDto;
	}

	// get user by id
	@Override
	public UserDto getUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", Long.toString(id)));
		return userMapper.mapToDto(user);
	}

	@Override
	public UserDto getUserByUsername(String username) {
		User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", username));
		return userMapper.mapToDto(user);
	}

	// update user
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
		user.setAbout(userDto.getAbout());
		user.setPhone(userDto.getPhone());
		user.setActive(true);
		User updatedUser = userRepository.save(user);
		return userMapper.mapToDto(updatedUser);
	}

	// delete user
	@Override
	public void deleteUser(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", Long.toString(id)));
		userRepository.delete(user);
	}
}
