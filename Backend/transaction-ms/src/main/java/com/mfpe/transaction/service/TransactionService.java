package com.mfpe.transaction.service;

import java.util.List;

import com.mfpe.transaction.dto.TransactionStatus;
import com.mfpe.transaction.entity.TransactionsHistory;
import com.mfpe.transaction.model.ServiceCharge;

public interface TransactionService {

	/**
	 * Service Layer interface for Transaction Microservice
	 */
	TransactionStatus deposit(String accountId, double amount);

	TransactionStatus withdraw(String accountId, double amount, String token);

	TransactionStatus transfer(String sourceAccountId, String destinationAccountId, double amount, String token);

	List<TransactionsHistory> getTransactions(String accountId, String token);
	
	List<ServiceCharge> getServiceCharges(String customerId, String token);

}
