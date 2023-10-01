package com.zesty.ecom.Payload.Dto;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductDto {
	
	private Long productId;
	private String name;
	private String brand;
	private String color;
	private Double price;
	private Boolean inStock;
	private Double discountPercent;
	private boolean live;
	private String description;
	private CategoryDto category;
	private List<ProductImageDto> images;
	private Set<SizesDto> sizes;
//	private CartItem cartItem; not needed cartItem here in product

}
