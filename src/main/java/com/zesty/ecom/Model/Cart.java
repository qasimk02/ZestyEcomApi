package com.zesty.ecom.Model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.zesty.ecom.Util.LocalDateTimeConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="cart")
public class Cart {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="cart_id")
	private Long cartId;
	
	@Column(name="total_price",columnDefinition = "double default 0.0")
	private Double totalPrice;
	
	@Column(name="total_discounted_price",columnDefinition = "double default 0.0")
	private Double totalDiscountedPrice;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	@Convert(converter = LocalDateTimeConverter.class)
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	@Convert(converter = LocalDateTimeConverter.class)
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
	@OneToMany(mappedBy="cart",fetch=FetchType.EAGER,cascade=CascadeType.ALL,orphanRemoval = true)
	private Set<CartItem> cartItems = new HashSet<>();
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;

}
