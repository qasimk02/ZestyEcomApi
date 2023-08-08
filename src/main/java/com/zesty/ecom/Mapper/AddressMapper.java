package com.zesty.ecom.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.zesty.ecom.Model.Address;
import com.zesty.ecom.Payload.Dto.AddressDto;

public class AddressMapper {

	@Autowired
	private ModelMapper modelMapper;
	
	public AddressDto mapToDto(Address address) {
		return modelMapper.map(address,AddressDto.class);
	}
	
	public Address mapToEntity(AddressDto addressDto) {
		return modelMapper.map(addressDto, Address.class);
	}
	
}
