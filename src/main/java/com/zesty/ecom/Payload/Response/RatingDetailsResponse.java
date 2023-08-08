package com.zesty.ecom.Payload.Response;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingDetailsResponse {
	
	
	private Long totalRatings;
	private Double averageRating;
	private HashMap<Integer,Long> totalRatingsByValue;
	private HashMap<Integer,Double> ratingPercentByValue;
	

}
