package com.zesty.ecom.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zesty.ecom.Payload.Dto.UserDto;
import com.zesty.ecom.Payload.Response.ApiResponse;
import com.zesty.ecom.Service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	// create user
	@PostMapping("")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		UserDto savedUser = userService.createUser(userDto);
		System.out.println(savedUser.getCreatedAt());
		return new ResponseEntity<UserDto>(savedUser, HttpStatus.CREATED);
	}

	// get all users
	@GetMapping("")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		List<UserDto> users = userService.getAllUsers();
		return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
	}

	// get user by id
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
		UserDto user = userService.getUserById(id);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	// get user by username
	@GetMapping("/profile")
	public ResponseEntity<UserDto> getUserByUsername(Principal principal) {
		UserDto user = userService.getUserByUsername(principal.getName());
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	// update user
	@PutMapping("/{id}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto user, @PathVariable("id") Long id) {
		UserDto updatedUser = userService.updateUser(user, id);
		return new ResponseEntity<UserDto>(updatedUser, HttpStatus.OK);
	}

	// delete user
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("id") Long id) {
		userService.deleteUser(id);
		ApiResponse apiResponse = new ApiResponse("User deleted succesfully", true);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

}
