package com.zesty.ecom.Testing;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {
	
	@PostMapping("/login")
	public ResponseEntity<Map<String,Boolean>> login(@RequestBody Test test){
		System.out.println(test);
		Map<String,Boolean> map = new HashMap<>();
		
		map.put("success", true);
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
	
}
