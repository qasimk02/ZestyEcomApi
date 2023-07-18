package com.zesty.ecom.Exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.zesty.ecom.Payload.ApiResponse;
import com.zesty.ecom.Payload.MultipleApiResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	// handling resource not found exception
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
		String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(message, false);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DuplicateFieldExcepiton.class)
	public ResponseEntity<ApiResponse> duplicateFieldExceptionHandler(DuplicateFieldExcepiton ex) {
		String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(message, false);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.CONFLICT);
	}

	// validation exception handler
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<MultipleApiResponse> handleConstraintViolationException(ConstraintViolationException ex) {
		// Process the constraint violation exception and build a custom response
		List<String> errors = new ArrayList<>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			errors.add(violation.getMessage());
		}

		// Build the response object with the error details
		MultipleApiResponse multipleApiResponses = new MultipleApiResponse(errors, false);

		return new ResponseEntity<>(multipleApiResponses, HttpStatus.BAD_REQUEST);
	}

	// handling excepiton when n number of product does not match
	// handling resource not found exception
	@ExceptionHandler(CategoriesNotFoundExcepiton.class)
	public ResponseEntity<MultipleApiResponse> handleCategoryNotFoundException(CategoriesNotFoundExcepiton ex) {
		List<String> errors = ex.getErrorMessages();
		MultipleApiResponse multipleApiResponses = new MultipleApiResponse(errors, false);
		return new ResponseEntity<>(multipleApiResponses, HttpStatus.BAD_REQUEST);
	}

}
