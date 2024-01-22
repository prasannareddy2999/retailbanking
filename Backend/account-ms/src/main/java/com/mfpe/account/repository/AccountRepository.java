package com.mfpe.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mfpe.account.entity.Account;

public interface AccountRepository extends JpaRepository<Account, String>{
	
	/**
	 * Repository Layer class for Account Entity class
	 * */
	List<Account> findByCustomerId(String customerId);
	
}
