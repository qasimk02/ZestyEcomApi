package com.zesty.ecom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zesty.ecom.Model.Cart;
import com.zesty.ecom.Model.CartItem;
import com.zesty.ecom.Model.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{

	CartItem findByProduct(Product p);

	Boolean existsByProductAndCart(Product p, Cart c);

}
