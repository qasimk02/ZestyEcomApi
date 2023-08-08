package com.zesty.ecom.Payload.Dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {
	
	private Long orderId;
	private LocalDateTime orderDate;
	private LocalDateTime deliveryDate;
	private Double totalPrice;
	private Double totalDiscountedPrice;
	private Double shippingCharge;
	private String orderStatus;
	private Integer totalItems;	
	
//	private UserDto user;
	private List<OrderItemDto> orderItems;
	private AddressDto shippingAddress;
	private PaymentDetailsDto paymentDetails;
	
}
