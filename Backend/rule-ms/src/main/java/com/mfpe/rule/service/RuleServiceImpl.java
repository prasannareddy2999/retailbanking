package com.mfpe.rule.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mfpe.rule.dto.RuleStatus;
import com.mfpe.rule.dto.ServiceCharge;
import com.mfpe.rule.exception.TokenNotFoundException;
import com.mfpe.rule.feign.AccountFeign;
import com.mfpe.rule.model.AccountDto;

@Service
public class RuleServiceImpl implements RuleService {

	/**
	 * Service Layer class for Rule Microservice
	 */
	@Autowired
	private AccountFeign accountClient;

//	@Override
//	public RuleStatus evaluateMinBal(double amount, String accountId, String token) {
//		
//		if(token == null) {
//			throw new TokenNotFoundException();
//		}
//		
//		int min = 1000;
//
//		ResponseEntity<AccountDto> account = accountClient.getAccount(token, accountId);
//		double balance = account.getBody().getBalance();
//
//		double diff = balance - amount;
//		if (diff >= min) {
//			return new RuleStatus("Allowed", "Completely OK");
//		} else if (diff >= 0) {
//			return new RuleStatus("Allowed", "Partially OK");
//		} else {
//			return new RuleStatus("Denied", "Not OK");
//		}
//
//	}

	/**
	 * @param token
	 * @param accountId
	 * @param amount
	 * @return RuleStatus that contains a status and message about the current
	 *         transaction
	 */
	@Override
	public RuleStatus evaluateMinBal(double amount, String accountId, String token) {

		// if token is null
		if (token == null) {

			// the application raises the respective exception
			throw new TokenNotFoundException();
		}

		// creating a local variables for minimum balance
		int savingsMin = 5000;
		int currentMin = 11000;

		// requesting the Account feign to get account details
		ResponseEntity<AccountDto> account = accountClient.getAccount(token, accountId);

		// fetching the current balance
		double balance = account.getBody().getBalance();

		// if account is savings type
		if (accountId.contains("SBACS")) {

			// deduct the amount from balance, but this deduction changes will not get
			// persisted
			double diff = balance - amount;

			// if result is greater than minimum balance
			if (diff >= savingsMin) {

				// returns completely ok status
				return new RuleStatus("Allowed", "Completely OK");

				// if result is greater than 0
			} else if (diff >= 0) {

				// returns partially ok status
				return new RuleStatus("Allowed", "Partially OK");

				// if result is less than 0
			} else {

				// returns not ok status
				return new RuleStatus("Denied", "Not OK");
			}

			// if account is current type
		} else {

			// deduct the amount from balance, but this deduction changes will not get
			// persisted
			double diff = balance - amount;

			// if result is greater than minimum balance
			if (diff >= currentMin) {

				// returns completely ok status
				return new RuleStatus("Allowed", "Completely OK");

				// if result is greater than 0
			} else if (diff >= 0) {

				// returns partially ok status
				return new RuleStatus("Allowed", "Partially OK");

				// if result is less than 0
			} else {

				// returns not ok status
				return new RuleStatus("Denied", "Not OK");
			}
		}

	}

	@Override
	public List<ServiceCharge> getServiceCharges(String customerId, String token) {

		if (token == null) {
			throw new TokenNotFoundException();
		}

		// ResponseEntity<AccountDto> account = accountClient.getAccount(token,
		// accountId);
		ResponseEntity<List<AccountDto>> accountList = accountClient.getCustomerAccounts(customerId);

		String savingsAccountId = accountList.getBody().get(0).getAccountId();
		String currentAccountId = accountList.getBody().get(1).getAccountId();
		double savingsAccountBalance = accountList.getBody().get(0).getBalance();
		double currentAccountBalance = accountList.getBody().get(1).getBalance();

		// double balance = account.getBody().getBalance();

		// double minBalance = 5000;

		ServiceCharge savingsAccountCharges = new ServiceCharge();
		ServiceCharge currentAccountCharges = new ServiceCharge();

		if (savingsAccountBalance < 5000) {
			savingsAccountCharges.setAccountId(savingsAccountId);
			savingsAccountCharges.setMessage(
					"Your Savings Account is not satisfying the minimum balance criteria, ₹200 will be detucted.");
			savingsAccountCharges.setBalance(savingsAccountBalance);
		} else {
			savingsAccountCharges.setAccountId(savingsAccountId);
			savingsAccountCharges.setMessage("maintaining minimum amount, no detection.");
			savingsAccountCharges.setBalance(savingsAccountBalance);
		}

		if (currentAccountBalance < 11000) {
			currentAccountCharges.setAccountId(currentAccountId);
			currentAccountCharges.setMessage(
					"Your Current Account is not satisfying the minimum balance criteria, ₹800 will be detucted.");
			currentAccountCharges.setBalance(currentAccountBalance);
		} else {
			currentAccountCharges.setAccountId(currentAccountId);
			currentAccountCharges.setMessage("maintaining minimum amount, no detection.");
			currentAccountCharges.setBalance(currentAccountBalance);
		}

		List<ServiceCharge> serviceChargesList = new ArrayList<>();

		serviceChargesList.add(savingsAccountCharges);
		serviceChargesList.add(currentAccountCharges);

		return serviceChargesList;

	}

}
