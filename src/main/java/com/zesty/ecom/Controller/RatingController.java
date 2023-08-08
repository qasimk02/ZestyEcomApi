package com.zesty.ecom.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zesty.ecom.Payload.Response.RatingDetailsResponse;
import com.zesty.ecom.Service.RatingService;

@RestController
@RequestMapping("/api/rating")
public class RatingController {

	@Autowired
	private RatingService ratingService;
	
	@GetMapping("/{pId}")
	public ResponseEntity<RatingDetailsResponse> getRatingDetails(@PathVariable("pId") Long pId){
		
		RatingDetailsResponse ratingDetails = this.ratingService.getRatingsDetailsByProduct(pId);
		
		return new ResponseEntity<RatingDetailsResponse>(ratingDetails,HttpStatus.OK);
	}
	
}
