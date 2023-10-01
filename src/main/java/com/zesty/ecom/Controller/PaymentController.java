package com.zesty.ecom.Controller;

import java.security.Principal;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.zesty.ecom.Exception.ResourceNotFoundException;
import com.zesty.ecom.Model.Order;
import com.zesty.ecom.Model.User;
import com.zesty.ecom.Payload.Response.ApiResponse;
import com.zesty.ecom.Payload.Response.PaymentLinkResponse;
import com.zesty.ecom.Repository.OrderRepository;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	@Value("${razorpay.api.key}")
	String apiKey;

	@Value("${razorpay.api.secret}")
	String apiSecret;

	@Autowired
	private OrderRepository orderRepository;

	@PostMapping("/{orderId}")
	public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable("orderId") Long orderId,
			Principal principal) throws RazorpayException {

		Order order = this.orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "Id", Long.toString(orderId)));

		User user = order.getUser();

		try {
			RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

			// payment link request object
			JSONObject paymentLinkRequest = new JSONObject();
			paymentLinkRequest.put("amount", order.getTotalDiscountedPrice() * 100);
			paymentLinkRequest.put("currency", "INR");

			// customer object
			JSONObject customer = new JSONObject();
			customer.put("name", user.getFirstName() + " " + user.getLastName());
			customer.put("email", user.getEmail());
			paymentLinkRequest.put("customer", customer);

			// Notification object
			JSONObject notify = new JSONObject();
			notify.put("sms", true);
			notify.put("email", true);
			paymentLinkRequest.put("notify", notify);

			paymentLinkRequest.put("callback_url", "http://localhost:3000/payment/" + orderId);
			paymentLinkRequest.put("callback_method", "get");

			// creating payment link
			PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);

			// getting payment link details
			String paymentLinkId = payment.get("id");
			String paymentLinkUrl = payment.get("short_url");

			// setting response of payment link
			PaymentLinkResponse response = new PaymentLinkResponse();
			response.setPaymentLinkId(paymentLinkId);
			response.setPaymentLinkUrl(paymentLinkUrl);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			throw new RazorpayException(ex.getMessage());
		}
	}

	@GetMapping("/update")
	public ResponseEntity<ApiResponse> redirect(@RequestParam("payment_id") String paymentId,
			@RequestParam("payment_link_id") String paymentLinkId, 
			@RequestParam("payment_status") String paymentStatus,
			@RequestParam("order_id") Long orderId) throws RazorpayException {

		Order order = this.orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "Id", Long.toString(orderId)));

		RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

		try {
			Payment payment = razorpay.payments.fetch(paymentId);
			if (payment.get("status").equals("captured")) {
				order.getPaymentDetails().setRozarpayPaymentId(paymentId);
				order.getPaymentDetails().setRozarpayPaymentLinkStatus(paymentStatus);
				order.getPaymentDetails().setStatus("COMPLETED");
				order.getPaymentDetails().setRozarpayPaymentLinkId(paymentLinkId);
				order.getPaymentDetails().setPaymentMethod(payment.get("method"));
				order.setOrderStatus("PLACED");
				this.orderRepository.save(order);
			}
			ApiResponse apiResponse = new ApiResponse("Your order get Placed", orderId, true);
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.ACCEPTED);

		} catch (Exception ex) {
			throw new RazorpayException(ex.getMessage());
		}

	}

}
