package com.zesty.ecom.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.zesty.ecom.Model.User;
import com.zesty.ecom.Payload.Dto.UserDto;

public class UserMapper {
	@Autowired
	private ModelMapper modelMapper;
	
	public UserDto mapToDto(User user) {
		return modelMapper.map(user,UserDto.class);
	}
	
	public User mapToEntity(UserDto userDto) {
		return modelMapper.map(userDto, User.class);
	}
}
