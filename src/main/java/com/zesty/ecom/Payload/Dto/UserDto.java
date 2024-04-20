package com.zesty.ecom.Payload.Dto;

import java.time.LocalDateTime;

import com.zesty.ecom.Model.Role;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {
	private Long userId;

	private String email;

	private String password;

	private String firstName;

	private String lastName;

	private String about;

	// have to work more and add later
//	private String gender;

	private String phone;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private boolean active;

	private Role role;
	
	//private List<Address> not needed
	//private Cart cart; //not needed
}
