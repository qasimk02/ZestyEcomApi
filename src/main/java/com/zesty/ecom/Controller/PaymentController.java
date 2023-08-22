package com.zesty.ecom.Controller;

import java.security.Principal;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.zesty.ecom.Exception.ResourceNotFoundException;
import com.zesty.ecom.Model.Order;
import com.zesty.ecom.Model.User;
import com.zesty.ecom.Payload.Response.PaymentLinkResponse;
import com.zesty.ecom.Repository.OrderRepository;
import com.zesty.ecom.Service.OrderService;
import com.zesty.ecom.Service.UserService;

@RestController
@RequestMapping("/api")
public class PaymentController {
	
	@Value("{razorpay.api.key}")
	String apiKey;
	
	@Value("{razorpay.api.secret}")
	String apiSecret;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/payments/{orderId}")
	public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable("orderId") Long orderId,Principal principal) throws RazorpayException{
		
		Order order = this.orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "Id", Long.toString(orderId)));
		
		User user = order.getUser();
		
		try {
			RazorpayClient razorpay = new RazorpayClient(apiKey,apiSecret);
			
			//payment link request object
			JSONObject paymentLinkRequest = new JSONObject();
			paymentLinkRequest.put("amount", order.getTotalPrice());
			paymentLinkRequest.put("currencty", "INR");
			
			//customer object
			JSONObject customer = new JSONObject();
			customer.put("name", user.getFirstName()+" "+user.getLastName());
			customer.put("email",user.getEmail());
			paymentLinkRequest.put("cusotmer", customer);
			
			//Notification object
			JSONObject notify = new JSONObject();
			notify.put("sms",true);
			notify.put("email", true);
			paymentLinkRequest.put("notify", notify);
			
			paymentLinkRequest.put("callbackUrl", "http://localhost:3000/payment/"+orderId);
			paymentLinkRequest.put("callbackMethod","get");
			
			//creating payment link
			PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);
			
			//getting payment link details
			String paymentLinkId = payment.get("id");
			String paymentLinkUrl = payment.get("short_url");
			
			//setting response of payment link
			PaymentLinkResponse response = new PaymentLinkResponse();
			response.setPaymentLinkId(paymentLinkId);
			response.setPaymentLinkUrl(paymentLinkUrl);
			return new ResponseEntity<>(response,HttpStatus.OK);
		}catch(Exception ex) {
			throw new RazorpayException(ex.getMessage());
		}
	}
	
	
}
