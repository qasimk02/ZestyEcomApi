package com.zesty.ecom.Payload.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDto {
	private Integer categoryId;
	private String title;
	//private List<Product> products; //not needed products here
}
