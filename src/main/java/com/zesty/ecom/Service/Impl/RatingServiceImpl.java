package com.zesty.ecom.Service.Impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zesty.ecom.Payload.Dto.RatingDto;
import com.zesty.ecom.Payload.Response.RatingDetailsResponse;
import com.zesty.ecom.Repository.RatingRepository;
import com.zesty.ecom.Service.RatingService;

@Service
public class RatingServiceImpl implements RatingService {

	@Autowired
	private RatingRepository ratingRepository;

	@Override
	public RatingDetailsResponse getRatingsDetailsByProduct(Long pId) {
		Long totalRatings = this.ratingRepository.getTotalRatings(pId);
		Double averageRating = this.ratingRepository.getAverageRating(pId);
		List<Object[]> totalRatingByValue = this.ratingRepository.getTotalRatingsByValue(pId);

		HashMap<Integer, Long> totalRatingsByValueMap = new HashMap<>();
		HashMap<Integer, Double> ratingPercentByValueMap = new HashMap<>();

		for (Object[] result : totalRatingByValue) {
			Integer key = (Integer) result[0];
			Long value = (Long) result[1];
			totalRatingsByValueMap.put(key, value);
			// calculating percentage
			ratingPercentByValueMap.put(key, (double) value / totalRatings); // numberofratingofvalue/totalrating
		}

		for (int i = 1; i <= 5; ++i) {
			totalRatingsByValueMap.putIfAbsent(i, 0L);
			ratingPercentByValueMap.putIfAbsent(i, 0.0);
		}

		RatingDetailsResponse ratingDetails = new RatingDetailsResponse();
		ratingDetails.setTotalRatings(totalRatings);
		ratingDetails.setAverageRating(averageRating);
		ratingDetails.setTotalRatingsByValue(totalRatingsByValueMap);
		ratingDetails.setRatingPercentByValue(ratingPercentByValueMap);

		return ratingDetails;
	}

	// not needed

	@Override
	public Long getTotalRatingsByProductAndValue(Long pId, Integer value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getTotalRatingsByProduct(Long pId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getAverageRatingByProduct(Long pId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getAverageRatingByProductAndValue(Long pId, Integer value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RatingDto getRatingById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RatingDto getRatingByUserAndProduct(Long uId, Long pId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RatingDto> getAllRatingByProduct(Long pId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RatingDto addRating(RatingDto rating) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RatingDto updateRating(RatingDto rating) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RatingDto deleteRating(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
