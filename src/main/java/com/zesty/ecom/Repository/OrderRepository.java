package com.zesty.ecom.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zesty.ecom.Model.Order;
import com.zesty.ecom.Model.User;

public interface OrderRepository extends JpaRepository<Order, Long>{

	List<Order> findByUser(User user);
	
	Optional<Order> findByOrderIdAndUser(Long orderId,User user);

}
