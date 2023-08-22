package com.zesty.ecom.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zesty.ecom.Exception.CustomException;
import com.zesty.ecom.Exception.ResourceNotFoundException;
import com.zesty.ecom.Model.Cart;
import com.zesty.ecom.Model.CartItem;
import com.zesty.ecom.Model.Product;
import com.zesty.ecom.Model.User;
import com.zesty.ecom.Repository.CartItemRepository;
import com.zesty.ecom.Repository.UserRepository;
import com.zesty.ecom.Service.CartItemService;

@Service
public class CartItemServiceImpl implements CartItemService {

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Override 
	public CartItem getCartItem(Cart cart,Product p,String size) {
		return this.cartItemRepository.findByCartAndProductAndSize(cart,p,size);
	}
	
	@Override
	public CartItem getCartItemById(Long id) {
		return this.cartItemRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Cart Item", "Id", Long.toString(id)));
	}
	
	@Override
	public Boolean isCartItemExist(Product p,Cart c,String size) {
		return this.cartItemRepository.existsByCartAndProductAndSize(c,p,size);
	}

	@Override
	public Long deleteCartItem(Long cartItemId, String username) {
		CartItem cartItem = this.cartItemRepository.findById(cartItemId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart Item", "Id", Long.toString(cartItemId)));
		User user = this.userRepository.findByEmail(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Email", username));

		Cart cart = cartItem.getCart();
		cart.setTotalDiscountedPrice(cart.getTotalDiscountedPrice()-cartItem.getTotalDiscountedPrice());
		cart.setTotalPrice(cart.getTotalPrice()-cartItem.getTotalPrice());

		if (!cart.getUser().getUserId().equals(user.getUserId())) {
			throw new CustomException("You can't delete this cart item");
		}
		cart.getCartItems().remove(cartItem);
		this.cartItemRepository.delete(cartItem);
		return cartItemId;

	}

}
