package com.zesty.ecom.Exception;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("serial")
public class CategoriesNotFoundExcepiton extends RuntimeException {
	private List<String> errorMessages;

	public CategoriesNotFoundExcepiton(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}
}
