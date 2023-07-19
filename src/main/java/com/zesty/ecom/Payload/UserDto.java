package com.zesty.ecom.Payload;

import java.time.LocalDateTime;

import com.zesty.ecom.Model.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
	private long userId;

	// can be included in future
//	private String userName;

	private String email;

	private String password;

	private String firstName;

	private String lastName;

	// can be modify into another class as we need multiple address;
	private String address;

	private String about;

	// have to work more and add later
//	private String gender;

	private String phone;

	private LocalDateTime created;

	private LocalDateTime updated;

	private boolean active;

	private Role role;
}
