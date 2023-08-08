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
	public CartItem getCartItemByProduct(Product p) {
		return this.cartItemRepository.findByProduct(p);
	}
	@Override
	public Boolean isCartItemExist(Product p,Cart c) {
		return this.cartItemRepository.existsByProductAndCart(p,c);
	}

	@Override
	public void deleteCartItem(Long cartItemId, String username) {
		CartItem cartItem = this.cartItemRepository.findById(cartItemId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart Item", "Id", Long.toString(cartItemId)));
		User user = this.userRepository.findByEmail(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Email", username));

		Cart cart = cartItem.getCart();

		if (!cart.getUser().getUserId().equals(user.getUserId())) {
			throw new CustomException("You can't delete this cart item");
		}
		cart.getCartItems().remove(cartItem);
		this.cartItemRepository.delete(cartItem);

	}

}
