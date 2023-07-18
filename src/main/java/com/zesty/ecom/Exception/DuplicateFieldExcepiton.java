package com.zesty.ecom.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("serial")
public class DuplicateFieldExcepiton extends RuntimeException {
	
	private String resourceName;
	private String fieldName;
	private String fieldValue;

	public DuplicateFieldExcepiton(String resourceName,String fieldName,String fieldValue) {
		super(String.format("%s with %s %s already exist", resourceName,fieldName,fieldValue));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	
}
