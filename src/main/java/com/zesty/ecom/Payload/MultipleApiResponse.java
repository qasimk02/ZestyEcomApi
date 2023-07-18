package com.zesty.ecom.Payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MultipleApiResponse {
	
	private List<String> messages;
	private boolean success;
}
