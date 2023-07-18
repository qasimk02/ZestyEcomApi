package com.zesty.ecom.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
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
	private int productId;
	
	@Column(name="product_name",nullable=false)
	@NotBlank(message="Product name is required")
	private String productName;
	
	@Column(name="product_prize",nullable=false)
	@DecimalMin(value = "0.0", inclusive = false, message = "Product price must be greater than 0.0")
	private double productPrize;
	
	@Column(name="is_in_stock")
	private boolean inStock;
	
	@Column(name="product_quantity")
	private int productQuantity;
	
	@Column(name="is_live")
	private boolean live;
	
	@Column(name="product_image_name")
	private String productImageName;
	
	@Column(name="product_desc")
	@Size(max=1000,message="Maximum allowed length is of 1000 letters")
	private String productDesc;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JsonManagedReference
	@JsonIgnore
	@JoinColumn(name="category_id")
	private Category category;
}
