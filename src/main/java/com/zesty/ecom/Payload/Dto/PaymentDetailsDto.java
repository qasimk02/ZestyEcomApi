package com.zesty.ecom.Payload.Dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class PaymentDetailsDto {
	
	@Column(name="payment_method")
	private String paymentMethod;
	
	@Column(name="payment_status")
	private String status;
	
	@Column(name="payment_link_id")
	private String rozarpayPaymentLinkId;
	
	@Column(name="payment_reference_id")
	private String rozarpayPaymentRefereceId;
	
	@Column(name="payment_link_status")
	private String rozarpayPaymentLinkStatus;
	
	@Column(name="payment_id")
	private String rozarpayPaymentId;
	
	
	
	
}
