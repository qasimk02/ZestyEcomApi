package com.zesty.ecom.Service.Impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zesty.ecom.Exception.CustomException;
import com.zesty.ecom.Exception.ResourceNotFoundException;
import com.zesty.ecom.Mapper.AddressMapper;
import com.zesty.ecom.Mapper.OrderMapper;
import com.zesty.ecom.Model.Address;
import com.zesty.ecom.Model.Cart;
import com.zesty.ecom.Model.CartItem;
import com.zesty.ecom.Model.Order;
import com.zesty.ecom.Model.OrderItem;
import com.zesty.ecom.Model.PaymentDetails;
import com.zesty.ecom.Model.User;
import com.zesty.ecom.Payload.Dto.AddressDto;
import com.zesty.ecom.Payload.Dto.OrderDto;
import com.zesty.ecom.Repository.AddressRepository;
import com.zesty.ecom.Repository.OrderRepository;
import com.zesty.ecom.Repository.UserRepository;
import com.zesty.ecom.Service.CartService;
import com.zesty.ecom.Service.OrderService;
import com.zesty.ecom.Util.OrderStatus;
import com.zesty.ecom.Util.PaymentStatus;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AddressMapper addressMapper;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CartService cartSerivce;

	@Autowired
	private OrderMapper orderMapper;

	@Override
	@Transactional
	public OrderDto createOrderFromCart(AddressDto shippingAddress, String username) {
		User user = this.userRepository.findByEmail(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Email", username));
		Address addressToBeSaved = this.addressMapper.mapToEntity(shippingAddress);
		addressToBeSaved.setUser(user);

		Cart cart = user.getCart();
		Set<CartItem> cartItems = cart.getCartItems();
		if (cart == null || cartItems.size() == 0) {
			throw new CustomException("You don't have anything to order in cart");
		}

		// saving the address
		Address address = this.addressRepository.save(addressToBeSaved);

		// orderItems
		List<OrderItem> orderItems = new ArrayList<>();
		for (CartItem item : cartItems) {
			OrderItem orderItem = new OrderItem();
			orderItem.setTotalPrice(item.getTotalPrice());
			orderItem.setTotalDiscountedPrice(item.getTotalDiscountedPrice());
			orderItem.setProduct(item.getProduct());
			orderItem.setQuantity(item.getQuantity());
			orderItem.setSize(item.getSize());
			orderItem.setUser(user);
			orderItems.add(orderItem);
		}

		

		Order order = new Order();
		order.setUser(user);
		order.setOrderItems(orderItems);
		order.setTotalPrice(cart.getTotalPrice());
		order.setTotalDiscountedPrice(preciseUpto2Decimal(cart.getTotalDiscountedPrice()));
		order.setShippingCharge(0.0); // delivery charge free
		order.setTotalItems(cartItems.size());
		order.setShippingAddress(address);
		order.setOrderStatus(OrderStatus.PENDING);

		// payment details
		PaymentDetails paymentDetails = new PaymentDetails();
		paymentDetails.setStatus(PaymentStatus.PENDING);
		order.setPaymentDetails(paymentDetails);

		for (OrderItem item : orderItems) {
			item.setOrder(order);
		}

		Order savedOrder = orderRepository.save(order);
		
		this.cartSerivce.deleteCart(username);

		return orderMapper.mapToDto(savedOrder);
	}
	
	public Double preciseUpto2Decimal(Double value) {
		DecimalFormat df = new DecimalFormat("0.00");
		String formattedValue = df.format(value);
		return Double.parseDouble(formattedValue);
	}

	@Override
	public OrderDto findOrderById(Long orderId) {
		Order order = this.orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "Id", Long.toString(orderId)));
		return orderMapper.mapToDto(order);
	}
	
	@Override 
	public OrderDto findOrderByIdAndUser(Long orderId,String username) {
		User user = this.userRepository.findByEmail(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Email", username));
		Order order = this.orderRepository.findByOrderIdAndUser(orderId,user)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "Email", username));;
		OrderDto ordersDto = orderMapper.mapToDto(order);
		return ordersDto;
	}
	@Override
	public List<OrderDto> getUserOrderHistory(String username) {
		User user = this.userRepository.findByEmail(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Email", username));
		List<Order> orders = this.orderRepository.findByUser(user);
		List<OrderDto> ordersDto = orders.stream().map((o) -> orderMapper.mapToDto(o)).collect(Collectors.toList());
		return ordersDto;
	}

	@Override
	public List<OrderDto> getAllOrders() {
		List<Order> orders = this.orderRepository.findAll();
		List<OrderDto> ordersDto = orders.stream().map((o) -> orderMapper.mapToDto(o)).collect(Collectors.toList());
		return ordersDto;
	}

	@Override
	public OrderDto placeOrder(Long orderId) {
		Order order = this.orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "Id", Long.toString(orderId)));
		order.setOrderStatus(OrderStatus.PLACED);
		orderRepository.save(order);
		return orderMapper.mapToDto(order);
	}

	@Override
	public OrderDto confirmOrder(Long orderId) {
		Order order = this.orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "Id", Long.toString(orderId)));
		order.setOrderStatus(OrderStatus.CONFIRMED);
		orderRepository.save(order);
		return orderMapper.mapToDto(order);
	}

	@Override
	public OrderDto markOrderAsShipped(Long orderId) {
		Order order = this.orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "Id", Long.toString(orderId)));
		order.setOrderStatus(OrderStatus.SHIPPED);
		orderRepository.save(order);
		return orderMapper.mapToDto(order);
	}

	@Override
	public OrderDto markOrderAsDelivered(Long orderId) {
		Order order = this.orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "Id", Long.toString(orderId)));
		order.setOrderStatus(OrderStatus.DELIVERED);
		orderRepository.save(order);
		return orderMapper.mapToDto(order);
	}
	
	@Override
	public OrderDto markOrderAsOutForDelivery(Long orderId) {
		Order order = this.orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "Id", Long.toString(orderId)));
		order.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
		orderRepository.save(order);
		return orderMapper.mapToDto(order);
	}

	@Override
	public OrderDto cancelOrder(Long orderId) {
		Order order = this.orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "Id", Long.toString(orderId)));
		order.setOrderStatus(OrderStatus.CANCELLED);
		orderRepository.save(order);
		return orderMapper.mapToDto(order);
	}

	@Override
	public void deleteOrderByOrderId(Long orderId) {
		Order order = this.orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "Id", Long.toString(orderId)));
		orderRepository.delete(order);

	}

}
