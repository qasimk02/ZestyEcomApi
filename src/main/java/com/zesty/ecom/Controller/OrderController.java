package com.zesty.ecom.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zesty.ecom.Payload.Dto.AddressDto;
import com.zesty.ecom.Payload.Dto.OrderDto;
import com.zesty.ecom.Payload.Response.ApiResponse;
import com.zesty.ecom.Service.OrderService;



@RestController
@RequestMapping("/api/orders")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	//create
	@PostMapping("")
	public ResponseEntity<OrderDto> createOrder(@RequestBody AddressDto shippedAddress,Principal principal) {
		OrderDto createdOrder = this.orderService.createOrderFromCart(shippedAddress, principal.getName());
		return new ResponseEntity<>(createdOrder,HttpStatus.CREATED);
	}
	
	//get
	@GetMapping("{id}")
	public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") Long id,Principal prinicipal) {
		OrderDto order = this.orderService.findOrderByIdAndUser(id,prinicipal.getName());
		return new ResponseEntity<>(order,HttpStatus.CREATED);
	}
	
	@GetMapping("/history/user")
	public ResponseEntity<List<OrderDto>> getUserOrderHistory(Principal principal){
		List<OrderDto> orders = this.orderService.getUserOrderHistory(principal.getName());
		return new ResponseEntity<>(orders,HttpStatus.OK);
	}
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<OrderDto>> getAllOrders(){
		List<OrderDto> orders = this.orderService.getAllOrders();
		return new ResponseEntity<>(orders,HttpStatus.OK);
	}
	
	
	//update
	@PutMapping("/{id}/place")
	public ResponseEntity<OrderDto> placeOrder(@PathVariable("id") Long id){
		OrderDto order = this.orderService.placeOrder(id);
		return new ResponseEntity<>(order,HttpStatus.OK);
	}
	
	@PutMapping("/{id}/confirm")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<OrderDto> confirmOrder(@PathVariable("id") Long id){
		OrderDto order = this.orderService.confirmOrder(id);
		return new ResponseEntity<>(order,HttpStatus.OK);
	}
	
	@PutMapping("/{id}/mark-shipped")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<OrderDto> markOrderAsShipped(@PathVariable("id") Long id){
		OrderDto order = this.orderService.markOrderAsShipped(id);
		return new ResponseEntity<>(order,HttpStatus.OK);
	}
	
	@PutMapping("/{id}/cancel")
	public ResponseEntity<OrderDto> cancelOrder(@PathVariable("id") Long id){
		OrderDto order = this.orderService.cancelOrder(id);
		return new ResponseEntity<>(order,HttpStatus.OK);
	}
	
	@PutMapping("/{id}/deliver")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<OrderDto> deliverOrder(@PathVariable("id") Long id){
		OrderDto order = this.orderService.markOrderAsDelivered(id);
		return new ResponseEntity<>(order,HttpStatus.OK);
	}
	
	@PutMapping("/{id}/mark-out-for=delivery")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<OrderDto> markOrderAsOutForDelivery(@PathVariable("id") Long id){
		OrderDto order = this.orderService.markOrderAsOutForDelivery(id);
		return new ResponseEntity<>(order,HttpStatus.OK);
	}
	
	
	//delete
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteOrder(@PathVariable("id") Long id){
		this.orderService.deleteOrderByOrderId(id);
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage("Order deleted successfully");
		apiResponse.setSuccess(true);
		return new ResponseEntity<>(apiResponse,HttpStatus.OK);
	}
	
	
}
