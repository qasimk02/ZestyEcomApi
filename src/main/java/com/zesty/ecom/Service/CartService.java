package com.zesty.ecom.Service;

import com.zesty.ecom.Payload.Dto.CartDto;
import com.zesty.ecom.Payload.Request.ItemRequest;

public interface CartService {
	
	CartDto addItemToCart(ItemRequest item,String username);

	CartDto getCartByUsername(String username);

	CartDto getCartById(Long id);

	void deleteCart(String username);

	Long deleteCartItem(Long itemId, String username);
	
}
