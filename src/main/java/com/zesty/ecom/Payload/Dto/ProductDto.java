package com.zesty.ecom.Payload.Dto;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDto {
	
	private Long productId;
	private String name;
	private String brand;
	private String color;
	private Double price;
	private Boolean inStock;
	private Double discountPercent;
	private boolean live;
	private String imageName;
	private String description;
	private List<CategoryDto> categories;
	private Set<SizesDto> sizes;
//	private CartItem cartItem; not needed cartItem here in product

}
