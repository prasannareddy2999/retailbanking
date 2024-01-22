package com.mfpe.account.service;

import java.util.Date;
import java.util.List;

import com.mfpe.account.dto.AccountCreationStatus;
import com.mfpe.account.dto.AccountDto;
import com.mfpe.account.dto.Statement;
import com.mfpe.account.dto.TransactionStatus;

public interface AccountService {

	/**
	 * Service Layer interface for Account Microservice
	 */
	AccountCreationStatus createAccount(String customerId);

	AccountDto getAccount(String accountId, String customerId);

	List<AccountDto> getCustomerAccounts(String customerId);

	Statement getAccountStatement(String accountId, Date fromDate, Date toDate, String token);

	TransactionStatus deposit(String accountId, double amount);

	TransactionStatus withdraw(String accountId, double amount, String customerId);

	Statement getAccountStatement(String accountId, String token);

}
