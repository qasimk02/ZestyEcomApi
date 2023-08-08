package com.zesty.ecom.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zesty.ecom.Model.Cart;
import com.zesty.ecom.Model.User;

public interface CartRepository extends JpaRepository<Cart, Long>{

	Optional<Cart> findByUser(User user);

}
