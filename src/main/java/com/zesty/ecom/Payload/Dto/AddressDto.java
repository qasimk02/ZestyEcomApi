package com.zesty.ecom.Payload.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
	
	private Long addressId;
	private String fullName;
	private String phoneNumber;
	private String postalCode;
	private String streetAddress;
    private String optionalAddress;
    private String city;
    private String state;
    private String country;
	
}
