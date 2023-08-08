package com.zesty.ecom.Model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.zesty.ecom.Util.LocalDateTimeConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="orders")
@Getter
@Setter
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id", nullable = false, updatable = false)
	private Long orderId;
	
	@Column(name = "order_date", nullable = false, updatable = false)
	@Convert(converter = LocalDateTimeConverter.class)
	@CreationTimestamp
	private LocalDateTime orderDate;

	@Column(name = "delivery_date",updatable = false)
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime deliveryDate;
	
	@Column(name="total_price")
	private Double totalPrice;
	
	@Column(name="total_discounted_price")
	private Double totalDiscountedPrice;
	
	@Column(name="shipping_charge")
	private Double shippingCharge;
	
	@Column(name="order_status")
	private String orderStatus;
	
	@Column(name="total_items")
	private Integer totalItems;	
	
	@Embedded
	private PaymentDetails paymentDetails;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	@OneToMany(mappedBy = "order",cascade=CascadeType.ALL)
	private List<OrderItem> orderItems;
	
	@ManyToOne()
	@JoinColumn(name="shipping_address_id")
	private Address shippingAddress;
	
}
