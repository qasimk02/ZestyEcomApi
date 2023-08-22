package com.zesty.ecom.Model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.zesty.ecom.Util.LocalDateTimeConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="order_item")
@Getter
@Setter
public class OrderItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_item_id", nullable = false, updatable = false)
	private Long orderItemId;
	
	@Column(name="quantity")
	private Integer quantity;
	
	@Column(name="size")
	private String size;
	
	@Column(name="total_price")
	private Double totalPrice;
	
	@Column(name="discounted_price")
	private Double totalDiscountedPrice;
	
	@Column(name = "order_date", nullable = false, updatable = false)
	@Convert(converter = LocalDateTimeConverter.class)
	@CreationTimestamp
	private LocalDateTime orderDate;

	@Column(name = "delivery_date", updatable = false)
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime deliveryDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="order_id")
	private Order order;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="product_id")
	private Product product;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
}
