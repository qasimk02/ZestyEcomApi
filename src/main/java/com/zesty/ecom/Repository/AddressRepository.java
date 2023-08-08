package com.zesty.ecom.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zesty.ecom.Model.Address;
import com.zesty.ecom.Model.User;

public interface AddressRepository extends JpaRepository<Address, Long>{

	List<Address> findByUser(User user);

}
