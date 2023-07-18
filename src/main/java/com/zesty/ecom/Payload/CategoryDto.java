package com.zesty.ecom.Payload;

import java.util.List;

import com.zesty.ecom.Model.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDto {
	private int id;
	private String title;
	private List<Product> products;
	@Override
	public String toString() {
		return "CategoryDto [id=" + id + ", title=" + title + ", products=" + products + "]";
	}
}
