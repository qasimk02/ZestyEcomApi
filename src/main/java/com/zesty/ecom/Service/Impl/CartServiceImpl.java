package com.zesty.ecom.Service.Impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zesty.ecom.Exception.CustomException;
import com.zesty.ecom.Exception.ResourceNotFoundException;
import com.zesty.ecom.Mapper.CartMapper;
import com.zesty.ecom.Model.Cart;
import com.zesty.ecom.Model.CartItem;
import com.zesty.ecom.Model.Product;
import com.zesty.ecom.Model.User;
import com.zesty.ecom.Payload.Dto.CartDto;
import com.zesty.ecom.Payload.Request.ItemRequest;
import com.zesty.ecom.Repository.CartRepository;
import com.zesty.ecom.Repository.ProductRepository;
import com.zesty.ecom.Repository.UserRepository;
import com.zesty.ecom.Service.CartItemService;
import com.zesty.ecom.Service.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private CartMapper cartMapper;

	@Override
	public CartDto addItemToCart(ItemRequest item, String username) {
//		get item details from request
		Long pId = item.getProductId();
		Integer quantity = item.getQuantity();

//		get the user
		User user = this.userRepository.findByEmail(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Email", username));

//		get the product
		Product product = this.productRepository.findById(pId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "Id", Long.toString(pId)));

//		 check product in stock or not and live or not
		if (!(product.isInStock() && product.isLive())) {
			throw new CustomException("Item Out of Stock");
		}

//		get the cart if not exist create carts
		Cart cart = user.getCart();

//		checking if product already in cart or not if its already there then will
//		update that particular cartItem otherwise will add the cart item to cart

		Set<CartItem> items = cart.getCartItems();
		
//		totalPrice and totalDiscountedPrice
		Double totalPrice;
		Double totalDiscountedPrice;
		CartItem cartItem;
		Double totalCartPrice;
		Double totalCartDiscountedPrice;
		
		if (this.cartItemService.isCartItemExist(product,cart)) {
			cartItem = this.cartItemService.getCartItemByProduct(product);
			// calculate the totalPrice and totalDiscountedPrice if the item is already present
			totalPrice = quantity * product.getPrice();
			totalDiscountedPrice = totalPrice - (totalPrice * product.getDiscountPercent() * 0.01);
			totalCartDiscountedPrice = cart.getTotalDiscountedPrice() + totalDiscountedPrice - cartItem.getTotalDiscountedPrice();
			totalCartPrice = cart.getTotalPrice() + totalPrice - cartItem.getTotalPrice();
			cartItem.setQuantity(quantity);
			cartItem.setTotalPrice(totalPrice);
			cartItem.setTotalDiscountedPrice(totalDiscountedPrice);
			items.add(cartItem);
		} else {
			cartItem = new CartItem();
			// calculate the totalPrice and totalDiscountedPrice if the item is not present
			totalPrice = product.getPrice();
			totalDiscountedPrice = totalPrice - (totalPrice * product.getDiscountPercent()*0.01);
			totalCartDiscountedPrice = cart.getTotalDiscountedPrice() + totalDiscountedPrice;
			totalCartPrice = cart.getTotalPrice() + totalPrice;
			cartItem.setQuantity(1);
			cartItem.setTotalPrice(totalPrice);
			cartItem.setProduct(product);
			cartItem.setTotalDiscountedPrice(totalDiscountedPrice);
			cartItem.setCart(cart);
			items.add(cartItem);
		}

		//updating totalCartDiscountedprice and totalCartPrice
		cart.setTotalPrice(totalCartPrice);
		cart.setTotalDiscountedPrice(totalCartDiscountedPrice);
		
		// save the cart
		Cart savedCart = this.cartRepository.save(cart);
		CartDto savedCartDto = this.cartMapper.mapToDto(savedCart);
		savedCartDto.setTotalCartItems(savedCart.getCartItems().size());
		return savedCartDto;
	}

	@Override
	public CartDto getCartByUsername(String username) {
		User user = this.userRepository.findByEmail(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Email", username));
		Cart cart = user.getCart();
		CartDto cartDto = this.cartMapper.mapToDto(cart);
		cartDto.setTotalCartItems(cart.getCartItems().size());
		return cartDto;
	}

	@Override
	public CartDto getCartById(Long id) {
		Cart cart = this.cartRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "Id", Long.toString(id)));
		CartDto cartDto = this.cartMapper.mapToDto(cart);
		cartDto.setTotalCartItems(cart.getCartItems().size());
		return cartDto;
	}

	@Override
	public void deleteCart(String username) {
		User user = this.userRepository.findByEmail(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Email", username));
		Cart cart = user.getCart();
		cart.getCartItems().clear();
		cart.setTotalDiscountedPrice(0.0);
		cart.setTotalPrice(0.0);
		this.cartRepository.save(cart);
	}
	
	@Override
	public void deleteCartItem(Long itemId,String username) {
		
		this.cartItemService.deleteCartItem(itemId, username);
	}
}
