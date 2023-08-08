package com.zesty.ecom.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDetails {

	private String paymentMethod;
	
	private String status;
	
	private String rozarpayPaymentLinkId;
	
	private String rozarpayPaymentRefereceId;
	
	private String rozarpayPaymentLinkStatus;
	
	private String rozarpayPaymentId;
	
}
