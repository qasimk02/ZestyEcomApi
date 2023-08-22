package com.zesty.ecom.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zesty.ecom.Payload.Dto.CartDto;
import com.zesty.ecom.Payload.Request.ItemRequest;
import com.zesty.ecom.Payload.Response.ApiResponse;
import com.zesty.ecom.Service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@PostMapping("")
	public ResponseEntity<CartDto> addItemToCart(@RequestBody ItemRequest item,Principal principal){
		
		CartDto cart = this.cartService.addItemToCart(item, principal.getName()) ;
		return new ResponseEntity<>(cart,HttpStatus.CREATED);
	}
	
	@GetMapping("")
	public ResponseEntity<CartDto> getCart(Principal principal){
		CartDto cart = this.cartService.getCartByUsername(principal.getName());
		return new ResponseEntity<>(cart,HttpStatus.CREATED);
	}
	
	@DeleteMapping("")
	public ResponseEntity<ApiResponse> deleteCart(Principal principal){
		this.cartService.deleteCart(principal.getName());
		ApiResponse apiResponse = new ApiResponse("Cart deleted successfully",null,true);
		return new ResponseEntity<>(apiResponse,HttpStatus.OK);
	}
	
	@DeleteMapping("{itemId}")
	public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable("itemId") Long itemId,Principal principal){
		Long id = this.cartService.deleteCartItem(itemId, principal.getName());
		ApiResponse apiResponse = new ApiResponse("Cart item deleted successfully",id,true);
		return new ResponseEntity<>(apiResponse,HttpStatus.OK);
	}
	
}
