package com.mfpe.customer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mfpe.customer.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	/**
	 * Repository Layer class for Customer Entity class
	 * */
	
	@Query("Select c from Customer c where c.panNo = ?1")
	Optional<Customer> findByPanNo(String panNo);
	
	@Query(value = "SELECT max(id) from Customer")
	long max();
	
	@Query("Select c from Customer c where c.customerId = ?1")
	Optional<Customer> findByCustomerId(String customerId);

}
