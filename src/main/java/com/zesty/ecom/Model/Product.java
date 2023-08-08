package com.zesty.ecom.Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zesty.ecom.Util.LocalDateTimeConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
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
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="product")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="product_id")
	private Long productId;
	
	@Column(name="name",nullable=false)
	@NotBlank(message="Product name is required")
	private String name;
	
	@Column(name="price",nullable=false)
	@NotNull(message="Price can't be null")
	@DecimalMin(value = "0.0", inclusive = false, message = "Product price must be greater than 0.0")
	private Double price;
	
	@Column(name="discount_percent",nullable=false)
	@NotNull(message="Discount percent can't be null")
	@DecimalMin(value = "0.0", inclusive = true, message = "Product Discount percent must be greater than or equal to 0.0")
	private Double discountPercent;
	
	@Column(name="quantity")
	private Integer quantity;
	
	@Column(name="brand")
	private String brand;
	
	@Column(name="color")
	private String color;
	
	@Column(name="is_in_stock")
	private boolean inStock;
	
	@Column(name="is_live")
	private boolean live;
	
	@Column(name="image_name")
	private String imageName;
	
	@Column(name="description")
	@Size(max=1000,message="Maximum allowed length is of 1000 letters")
	private String description;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	@Convert(converter = LocalDateTimeConverter.class)
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	@Convert(converter = LocalDateTimeConverter.class)
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
	@Embedded
	@ElementCollection
	@Column(name="sizes")
	private Set<Sizes> sizes;
	
	//mapping
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="category_id")
	private Category category;
	
	@OneToMany(mappedBy="product",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonIgnore
	private Set<CartItem> cartItems;
	
	@OneToMany(mappedBy = "product",fetch=FetchType.LAZY)
	private Set<Rating> rating;
	
	@OneToMany(mappedBy = "product",fetch=FetchType.LAZY)
	private Set<Review> review;
	
	@OneToMany(mappedBy = "product",fetch=FetchType.LAZY) 
	private List<OrderItem> orderItems;

}
