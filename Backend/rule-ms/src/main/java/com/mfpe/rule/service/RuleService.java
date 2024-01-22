package com.mfpe.rule.service;

import java.util.List;

import com.mfpe.rule.dto.RuleStatus;
import com.mfpe.rule.dto.ServiceCharge;

public interface RuleService {

	/**
	 * Service Layer interface for Rule Microservice
	 */
	public RuleStatus evaluateMinBal(double amount, String accountId, String token);

	public List<ServiceCharge> getServiceCharges(String customerId, String token);

}
