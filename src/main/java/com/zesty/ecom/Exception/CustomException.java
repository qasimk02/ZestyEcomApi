package com.zesty.ecom.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("serial")
public class CustomException extends RuntimeException{
	
	private String message;
	public CustomException(String message) {
		super(message);
		this.message = message;
	}
	
}
