package com.zesty.ecom.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zesty.ecom.Payload.Dto.AddressDto;
import com.zesty.ecom.Payload.Response.ApiResponse;
import com.zesty.ecom.Service.AddressService;

@RestController
@RequestMapping("/api/address")
public class AddressController {

	@Autowired
	private AddressService addressService;

	// get
	@GetMapping("/{id}")
	public ResponseEntity<AddressDto> getAddressById(@PathVariable("id") Long id) {

		AddressDto address = this.addressService.getAddressById(id);

		return new ResponseEntity<AddressDto>(address, HttpStatus.OK);

	}

	@GetMapping("")
	public ResponseEntity<List<AddressDto>> getAddressByUserName(Principal principal) {

		List<AddressDto> allAddress = this.addressService.getAddressByUserName(principal.getName());
		return new ResponseEntity<List<AddressDto>>(allAddress, HttpStatus.OK);
	}

	// add
	@PostMapping("")
	public ResponseEntity<AddressDto> addAddress(@RequestBody AddressDto address, Principal principal) {

		AddressDto savedAddress = this.addressService.addAddress(principal.getName(), address);
		return new ResponseEntity<AddressDto>(savedAddress, HttpStatus.CREATED);
	}
	
	//update
	@PutMapping("/{id}")
	public ResponseEntity<AddressDto> updateAddress(@PathVariable("id") Long id,Principal principal,@RequestBody AddressDto address){
		
		AddressDto updatedAddress = this.addressService.updateAddress(id, principal.getName(), address);
		return new ResponseEntity<AddressDto>(updatedAddress,HttpStatus.OK);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteAddress(@PathVariable("id") Long id,Principal principal){
		this.addressService.deleteAddress(id, principal.getName());
		ApiResponse apiResponse = new ApiResponse("Address Deleted Successfully",true);
		
		return new ResponseEntity<>(apiResponse,HttpStatus.OK);
	}

}
