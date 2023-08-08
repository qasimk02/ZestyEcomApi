package com.zesty.ecom.Model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="cart_item",uniqueConstraints = @UniqueConstraint(columnNames = {"cart_id", "product_id"}))
public class CartItem {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="cart_item_id")
	private Long cartItemId;
	
	@Column(name="quantity")
	private Integer quantity;
	
	@Column(name="total_price")
	private Double totalPrice;
	
	@Column(name="total_discounted_price")
	private Double totalDiscountedPrice;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	@Convert(converter = LocalDateTimeConverter.class)
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	@Convert(converter = LocalDateTimeConverter.class)
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cart_id")
	@JsonIgnore
	private Cart cart;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="product_id")
	private Product product;
	
}
