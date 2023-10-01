package com.zesty.ecom.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zesty.ecom.Model.Cart;
import com.zesty.ecom.Model.User;

public interface CartRepository extends JpaRepository<Cart, Long> {

	Optional<Cart> findByUser(User user);

	//to get cart items count
	@Query("SELECT COUNT(ci) FROM Cart c JOIN c.cartItems ci WHERE c.user.email = :username")
	Integer countCartItemsByUsername(@Param("username") String username);

}
