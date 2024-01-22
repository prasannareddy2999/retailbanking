package com.mfpe.transaction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mfpe.transaction.entity.TransactionsHistory;

public interface TransactionRepository extends JpaRepository<TransactionsHistory, String> {

	@Query(value = "SELECT max(id) from TransactionsHistory")
	long max();
	
	@Query("Select t.transactionId from TransactionsHistory t where t.id = ?1")
	Optional<TransactionsHistory> findById(long id);
	
	List<TransactionsHistory> findBySourceAccountId(String accountId);
	
	List<TransactionsHistory> findBySourceAccountIdOrDestinationAccountId(String sourceAccountId, String destinationAccountId);
	
}
