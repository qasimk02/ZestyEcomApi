package com.zesty.ecom.Payload;

import com.zesty.ecom.Model.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDto {
	
	private int productId;
	private String productName;
	private double productPrize;
	private boolean inStock;
	private int productQuantity;
	private boolean live;
	private String productImageName;
	private String productDesc;
	private Category category;
	

}
