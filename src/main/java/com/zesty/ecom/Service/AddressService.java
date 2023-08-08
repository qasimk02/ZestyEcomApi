package com.zesty.ecom.Service;

import java.util.List;

import com.zesty.ecom.Payload.Dto.AddressDto;

public interface AddressService {
	
	//get
	AddressDto getAddressById(Long id);
	
	List<AddressDto> getAddressByUserName(String username);
	
	//add
	AddressDto addAddress(String username, AddressDto addressDto);
	
	//update
	AddressDto updateAddress(Long id,String username, AddressDto addressDto);
	
	//delete
	void deleteAddress(Long id,String username);
	
	
}
