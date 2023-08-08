package com.zesty.ecom.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.zesty.ecom.Model.Role;
import com.zesty.ecom.Payload.Dto.RoleDto;

public class RoleMapper {
	
	@Autowired
	private ModelMapper mapper;
	
	public RoleDto mapToDto(Role role) {
		return mapper.map(role, RoleDto.class);
	}
	
	public Role mapToEntity(RoleDto roleDto) {
		return mapper.map(roleDto, Role.class);
	}
}
