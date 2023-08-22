package com.zesty.ecom.Service;

import java.util.List;

import com.zesty.ecom.Payload.Dto.RatingDto;
import com.zesty.ecom.Payload.Response.RatingDetailsResponse;

public interface RatingService {
	
	RatingDetailsResponse getRatingsDetailsByProduct(Long pId);

	//not needed as of now
	Long getTotalRatingsByProductAndValue(Long pId,Integer value);//1,2,3,4,5
	Long getTotalRatingsByProduct(Long pId);
	Double getAverageRatingByProduct(Long pId);
	Double getAverageRatingByProductAndValue(Long pId, Integer value);
	
	RatingDto getRatingById(Long id);
	
	RatingDto getRatingByUserAndProduct(Long uId,Long pId);
	List<RatingDto> getAllRatingByProduct(Long pId);
	
	//add
	RatingDto addRating(RatingDto rating);
	
	//update
	RatingDto updateRating(RatingDto rating);
	
	//delete
	RatingDto deleteRating(Long id);

}
