package com.zesty.ecom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zesty.ecom.Model.Cart;
import com.zesty.ecom.Model.CartItem;
import com.zesty.ecom.Model.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
	
	Boolean existsByCartAndProductAndSize(Cart cart, Product product, String size);
}