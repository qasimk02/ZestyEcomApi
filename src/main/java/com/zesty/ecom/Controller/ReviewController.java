package com.zesty.ecom.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zesty.ecom.Payload.Dto.ReviewDto;
import com.zesty.ecom.Service.ReviewService;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;

	@GetMapping("/{pId}")
	public ResponseEntity<List<ReviewDto>> getAllReviewsAndRatingByProduct(@PathVariable("pId") Long pId) {

		List<ReviewDto> review = this.reviewService.getAllReviewByProduct(pId);
		return new ResponseEntity<List<ReviewDto>>(review, HttpStatus.OK);

	}

	@PostMapping("/{pId}")
	public ResponseEntity<ReviewDto> addReviewByProduct(@RequestBody ReviewDto r, @PathVariable("pId") Long pId,
			Principal principal) {
		ReviewDto savedReview = this.reviewService.addReview(r, pId, principal.getName());
		return new ResponseEntity<ReviewDto>(savedReview, HttpStatus.OK);

	}

}
