package com.zesty.ecom.Service;

import com.zesty.ecom.Model.Cart;
import com.zesty.ecom.Model.CartItem;
import com.zesty.ecom.Model.Product;


public interface CartItemService {
	
	CartItem getCartItem(Cart cart, Product p, String size);
	CartItem getCartItemById(Long id);
	
	Long deleteCartItem(Long cartItemId,String username);

	Boolean isCartItemExist(Product p, Cart c,String size);
	
}
