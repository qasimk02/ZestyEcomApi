package com.zesty.ecom.Service;

import java.util.List;

import com.zesty.ecom.Payload.Dto.AddressDto;
import com.zesty.ecom.Payload.Dto.OrderDto;

public interface OrderService {
	
	//create
	OrderDto createOrderFromCart(AddressDto shippingAddress,String username);
	
	//get
	OrderDto findOrderById(Long orderId);
	OrderDto findOrderByIdAndUser(Long orderId, String username);
	List<OrderDto> getUserOrderHistory(String username);
	List<OrderDto> getAllOrders();
	
	//update
	OrderDto placeOrder(Long orderId);
	OrderDto confirmOrder(Long orderId);
	OrderDto markOrderAsShipped(Long orderId);
	OrderDto markOrderAsDelivered(Long orderId);
	OrderDto markOrderAsOutForDelivery(Long orderId);
	OrderDto cancelOrder(Long orderId);
	
	//delete
	void deleteOrderByOrderId(Long orderId);

	
	
	
	
	
}
