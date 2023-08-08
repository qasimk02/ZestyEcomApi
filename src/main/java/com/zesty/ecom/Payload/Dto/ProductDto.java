package com.zesty.ecom.Payload.Dto;


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
	private Double Price;
	private Boolean inStock;
	private Integer quantity;
	private Double discountPercent;
	private boolean live;
	private String imageName;
	private String description;
	private CategoryDto category;
	
//	private Set<Sizes> sizes;
//	private String topLevelCategory;
//	private String secondLevelCategory;
//	private String thirdLevelCategory;
	//private CartItem cartItem; not needed cartItem here in product

}
