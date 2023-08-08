package com.zesty.ecom.Service;

import com.zesty.ecom.Model.Cart;
import com.zesty.ecom.Model.CartItem;
import com.zesty.ecom.Model.Product;


public interface CartItemService {
	
	CartItem getCartItemByProduct(Product p);
	
	void deleteCartItem(Long cartItemId,String username);

	Boolean isCartItemExist(Product p, Cart c);
	
}
