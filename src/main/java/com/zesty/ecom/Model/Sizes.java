package com.zesty.ecom.Model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sizes {
	
	@Column(name="size_label",nullable=false)
	@NotNull(message="size label can't be null")
	private String name;
	
	@Column(name="quantity",nullable=false)
	@NotNull(message="quantity can't be null")
	private Integer quantity;
}