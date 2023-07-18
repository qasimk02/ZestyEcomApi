package com.zesty.ecom.Testing;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Test{
	String userName;
	String password;
	@Override
	public String toString() {
		return "Test [userName=" + userName + ", password=" + password + "]";
	}
	
}