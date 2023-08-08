package com.zesty.ecom.Service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zesty.ecom.Exception.CustomException;
import com.zesty.ecom.Exception.ResourceNotFoundException;
import com.zesty.ecom.Mapper.AddressMapper;
import com.zesty.ecom.Model.Address;
import com.zesty.ecom.Model.User;
import com.zesty.ecom.Payload.Dto.AddressDto;
import com.zesty.ecom.Repository.AddressRepository;
import com.zesty.ecom.Repository.UserRepository;
import com.zesty.ecom.Service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private AddressMapper addressMapper;

	@Override
	public AddressDto getAddressById(Long id) {
		Address address = this.addressRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "Id", Long.toString(id)));
		return addressMapper.mapToDto(address);
	}

	@Override
	public List<AddressDto> getAddressByUserName(String username) {
		User user = this.userRepository.findByEmail(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Username", username));
		List<Address> address = this.addressRepository.findByUser(user);

		List<AddressDto> addressDto = address.stream().map((a) -> addressMapper.mapToDto(a))
				.collect(Collectors.toList());

		return addressDto;
	}

	@Override
	public AddressDto addAddress(String username, AddressDto addressDto) {
		User user = this.userRepository.findByEmail(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Username", username));

		Address address = new Address();
		address.setCity(addressDto.getCity());
		address.setCountry(addressDto.getCountry());
		address.setFullName(addressDto.getFullName());
		address.setStreetAddress(addressDto.getStreetAddress());
		address.setOptionalAddress(addressDto.getOptionalAddress());
		address.setPhoneNumber(addressDto.getPhoneNumber());
		address.setPostalCode(addressDto.getPostalCode());
		address.setState(addressDto.getState());
		address.setUser(user);

		Address savedAddress = this.addressRepository.save(address);

		return addressMapper.mapToDto(savedAddress);
	}

	@Override
	public AddressDto updateAddress(Long id, String username, AddressDto addressDto) {
		User user = this.userRepository.findByEmail(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Username", username));

		Address address = this.addressRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "Id", Long.toString(id)));

//		if logged in user is not same as the user who added this address
		if(!address.getUser().getUserId().equals(user.getUserId())) {
			throw new CustomException("You can't update the address");
		}
		
		address.setCity(addressDto.getCity());
		address.setCountry(addressDto.getCountry());
		address.setFullName(addressDto.getFullName());
		address.setStreetAddress(addressDto.getStreetAddress());
		address.setOptionalAddress(addressDto.getOptionalAddress());
		address.setPhoneNumber(addressDto.getPhoneNumber());
		address.setPostalCode(addressDto.getPostalCode());
		address.setState(addressDto.getState());
		
		Address updatedAddress = this.addressRepository.save(address);
		
		return addressMapper.mapToDto(updatedAddress);
	}

	@Override
	public void deleteAddress(Long id,String username) {
		
		User user = this.userRepository.findByEmail(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Username", username));

		Address address = this.addressRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "Id", Long.toString(id)));

//		if logged in user is not same as the user who added this address
		if(!address.getUser().getUserId().equals(user.getUserId())) {
			throw new CustomException("You can't update the address");
		}
		
		this.addressRepository.delete(address);
		
	}

}
