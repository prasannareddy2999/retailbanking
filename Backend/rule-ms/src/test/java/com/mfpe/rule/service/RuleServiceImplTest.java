package com.mfpe.rule.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mfpe.rule.dto.RuleStatus;
import com.mfpe.rule.dto.ServiceCharge;
import com.mfpe.rule.exception.TokenNotFoundException;
import com.mfpe.rule.feign.AccountFeign;
import com.mfpe.rule.model.Account;
import com.mfpe.rule.model.AccountDto;
import com.mfpe.rule.model.AccountType;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class RuleServiceImplTest {

	@Mock
	private AccountFeign accountClient;

	@InjectMocks
	private RuleServiceImpl ruleService;

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testInvalidEvaluateMinBal() {

		Assertions.assertThrows(TokenNotFoundException.class,
				() -> this.ruleService.evaluateMinBal(1000.0, "SBACS000001", null));

	}

	@Test
	void testEvaluateMinBalSavingsClosingBalanceGreaterThan5000() {

		AccountDto accountDto = new AccountDto("SBACS000001", 8000.0);
		when(accountClient.getAccount("token", "SBACS000001"))
				.thenReturn(new ResponseEntity<AccountDto>(accountDto, HttpStatus.OK));

		RuleStatus ruleStatus = new RuleStatus("Allowed", "Completely OK");
		RuleStatus resultStatus = ruleService.evaluateMinBal(1000, "SBACS000001", "token");
		assertThat(ruleStatus.getMessage()).isEqualTo(resultStatus.getMessage());
		assertThat(ruleStatus.getStatus()).isEqualTo(resultStatus.getStatus());
	}

	@Test
	void testEvaluateMinBalSavingsClosingBalanceGreaterThanEqualto5000() {

		AccountDto accountDto = new AccountDto("SBACS000001", 8000.0);
		when(accountClient.getAccount("token", "SBACS000001"))
				.thenReturn(new ResponseEntity<AccountDto>(accountDto, HttpStatus.OK));

		RuleStatus ruleStatus = new RuleStatus("Allowed", "Completely OK");
		RuleStatus resultStatus = ruleService.evaluateMinBal(3000, "SBACS000001", "token");
		assertThat(ruleStatus.getMessage()).isEqualTo(resultStatus.getMessage());
		assertThat(ruleStatus.getStatus()).isEqualTo(resultStatus.getStatus());
	}

	@Test
	void testEvaluateMinBalSavingsClosingBalanceLessThan5000() {
		AccountDto accountDto = new AccountDto("SBACS000001", 8000.0);
		when(accountClient.getAccount("token", "SBACS000001"))
				.thenReturn(new ResponseEntity<AccountDto>(accountDto, HttpStatus.OK));

		RuleStatus ruleStatus = new RuleStatus("Allowed", "Partially OK");
		RuleStatus resultStatus = ruleService.evaluateMinBal(4000, "SBACS000001", "token");
		assertThat(ruleStatus.getMessage()).isEqualTo(resultStatus.getMessage());
		assertThat(ruleStatus.getStatus()).isEqualTo(resultStatus.getStatus());

	}

	@Test
	void testEvaluateMinBalSavingsClosingBalanceEqualtoZero() {
		AccountDto accountDto = new AccountDto("SBACS000001", 8000.0);
		when(accountClient.getAccount("token", "SBACS000001"))
				.thenReturn(new ResponseEntity<AccountDto>(accountDto, HttpStatus.OK));

		RuleStatus ruleStatus = new RuleStatus("Allowed", "Partially OK");
		RuleStatus resultStatus = ruleService.evaluateMinBal(8000, "SBACS000001", "token");
		assertThat(ruleStatus.getMessage()).isEqualTo(resultStatus.getMessage());
		assertThat(ruleStatus.getStatus()).isEqualTo(resultStatus.getStatus());

	}

	@Test
	void testEvaluateMinBalSavingsClosingBalanceLessThan0() {
		AccountDto accountDto = new AccountDto("SBACS000001", 8000.0);
		when(accountClient.getAccount("token", "SBACS000001"))
				.thenReturn(new ResponseEntity<AccountDto>(accountDto, HttpStatus.OK));

		RuleStatus ruleStatus = new RuleStatus("Denied", "Not OK");
		RuleStatus resultStatus = ruleService.evaluateMinBal(9000, "SBACS000001", "token");
		assertThat(ruleStatus.getMessage()).isEqualTo(resultStatus.getMessage());
		assertThat(ruleStatus.getStatus()).isEqualTo(resultStatus.getStatus());

	}

	@Test
	void testEvaluateMinBalCurrentClosingBalanceGreaterThan11000() {

		AccountDto accountDto = new AccountDto("SBACC000001", 15000.0);
		when(accountClient.getAccount("token", "SBACC000001"))
				.thenReturn(new ResponseEntity<AccountDto>(accountDto, HttpStatus.OK));

		RuleStatus ruleStatus = new RuleStatus("Allowed", "Completely OK");
		RuleStatus resultStatus = ruleService.evaluateMinBal(2000, "SBACC000001", "token");
		assertThat(ruleStatus.getMessage()).isEqualTo(resultStatus.getMessage());
		assertThat(ruleStatus.getStatus()).isEqualTo(resultStatus.getStatus());
	}

	@Test
	void testEvaluateMinBalCurrentClosingBalanceEqualto11000() {

		AccountDto accountDto = new AccountDto("SBACC000001", 15000.0);
		when(accountClient.getAccount("token", "SBACC000001"))
				.thenReturn(new ResponseEntity<AccountDto>(accountDto, HttpStatus.OK));

		RuleStatus ruleStatus = new RuleStatus("Allowed", "Completely OK");
		RuleStatus resultStatus = ruleService.evaluateMinBal(4000, "SBACC000001", "token");
		assertThat(ruleStatus.getMessage()).isEqualTo(resultStatus.getMessage());
		assertThat(ruleStatus.getStatus()).isEqualTo(resultStatus.getStatus());
	}

	@Test
	void testEvaluateMinBalCurrentClosingBalanceLessThan11000() {

		AccountDto accountDto = new AccountDto("SBACC000001", 15000.0);
		when(accountClient.getAccount("token", "SBACC000001"))
				.thenReturn(new ResponseEntity<AccountDto>(accountDto, HttpStatus.OK));

		RuleStatus ruleStatus = new RuleStatus("Allowed", "Partially OK");
		RuleStatus resultStatus = ruleService.evaluateMinBal(5000, "SBACC000001", "token");
		assertThat(ruleStatus.getMessage()).isEqualTo(resultStatus.getMessage());
		assertThat(ruleStatus.getStatus()).isEqualTo(resultStatus.getStatus());
	}

	@Test
	void testEvaluateMinBalCurrentClosingBalanceEqualtoZero() {

		AccountDto accountDto = new AccountDto("SBACC000001", 15000.0);
		when(accountClient.getAccount("token", "SBACC000001"))
				.thenReturn(new ResponseEntity<AccountDto>(accountDto, HttpStatus.OK));

		RuleStatus ruleStatus = new RuleStatus("Allowed", "Partially OK");
		RuleStatus resultStatus = ruleService.evaluateMinBal(15000, "SBACC000001", "token");
		assertThat(ruleStatus.getMessage()).isEqualTo(resultStatus.getMessage());
		assertThat(ruleStatus.getStatus()).isEqualTo(resultStatus.getStatus());
	}

	@Test
	void testEvaluateMinBalCurrentClosingBalanceLessThanZero() {

		AccountDto accountDto = new AccountDto("SBACC000001", 15000.0);
		when(accountClient.getAccount("token", "SBACC000001"))
				.thenReturn(new ResponseEntity<AccountDto>(accountDto, HttpStatus.OK));

		RuleStatus ruleStatus = new RuleStatus("Denied", "Not OK");
		RuleStatus resultStatus = ruleService.evaluateMinBal(20000, "SBACC000001", "token");
		assertThat(ruleStatus.getMessage()).isEqualTo(resultStatus.getMessage());
		assertThat(ruleStatus.getStatus()).isEqualTo(resultStatus.getStatus());
	}

	@Test
	void testInvalidGetServiceCharges() {

		Assertions.assertThrows(TokenNotFoundException.class,
				() -> this.ruleService.getServiceCharges("SBCU000001", null));

	}

	@Test
	void testGetServiceChargesBalanceLessThan5000And11000() {
		String customerId = "SBCU000001";
		String token = "token";

		List<AccountDto> accountList = new ArrayList<>();

		accountList.add(new AccountDto("SBACS000001", 200));
		accountList.add(new AccountDto("SBACC000001", 500));

		when(accountClient.getCustomerAccounts(customerId))
				.thenReturn(new ResponseEntity<List<AccountDto>>(accountList, HttpStatus.OK));

		List<ServiceCharge> serviceChargeList = new ArrayList<>();
		serviceChargeList.add(new ServiceCharge("SBACS000001",
				"Your Savings Account is not satisfying the minimum balance criteria, ₹200 will be detucted.", 200));
		serviceChargeList.add(new ServiceCharge("SBACC000001",
				"Your Current Account is not satisfying the minimum balance criteria, ₹800 will be detucted.", 500));

		List<ServiceCharge> result = ruleService.getServiceCharges(customerId, token);

		assertThat(serviceChargeList.get(0).getAccountId()).isEqualTo(result.get(0).getAccountId());

	}

	@Test
	void testGetServiceChargesBalanceLessThan5000AndGreaterThan11000() {
		String customerId = "SBCU000001";
		String token = "token";

		List<AccountDto> accountList = new ArrayList<>();

		accountList.add(new AccountDto("SBACS000001", 200));
		accountList.add(new AccountDto("SBACC000001", 12000));

		when(accountClient.getCustomerAccounts(customerId))
				.thenReturn(new ResponseEntity<List<AccountDto>>(accountList, HttpStatus.OK));

		List<ServiceCharge> serviceChargeList = new ArrayList<>();
		serviceChargeList.add(new ServiceCharge("SBACS000001",
				"Your Savings Account is not satisfying the minimum balance criteria, ₹200 will be detucted.", 200));
		serviceChargeList.add(new ServiceCharge("SBACC000001", "maintaining minimum amount, no detection.", 12000));

		List<ServiceCharge> result = ruleService.getServiceCharges(customerId, token);

		assertThat(serviceChargeList.get(0).getAccountId()).isEqualTo(result.get(0).getAccountId());

	}

	@Test
	void testGetServiceChargesBalanceGreaterThan5000AndLessThan1100() {
		String customerId = "SBCU000001";
		String token = "token";

		List<AccountDto> accountList = new ArrayList<>();

		accountList.add(new AccountDto("SBACS000001", 12000));
		accountList.add(new AccountDto("SBACC000001", 500));

		when(accountClient.getCustomerAccounts(customerId))
				.thenReturn(new ResponseEntity<List<AccountDto>>(accountList, HttpStatus.OK));

		List<ServiceCharge> serviceChargeList = new ArrayList<>();
		serviceChargeList.add(new ServiceCharge("SBACS000001", "maintaining minimum amount, no detection.", 12000));
		serviceChargeList.add(new ServiceCharge("SBACC000001",
				"Your Current Account is not satisfying the minimum balance criteria, ₹800 will be detucted.", 500));

		List<ServiceCharge> result = ruleService.getServiceCharges(customerId, token);

		assertThat(serviceChargeList.get(0).getAccountId()).isEqualTo(result.get(0).getAccountId());
	}

	@Test
	void testGetServiceChargesBalanceGreaterThan5000And1100() {
		String customerId = "SBCU000001";
		String token = "token";

		List<AccountDto> accountList = new ArrayList<>();

		accountList.add(new AccountDto("SBACS000001", 12000));
		accountList.add(new AccountDto("SBACC000001", 15000));

		when(accountClient.getCustomerAccounts(customerId))
				.thenReturn(new ResponseEntity<List<AccountDto>>(accountList, HttpStatus.OK));

		List<ServiceCharge> serviceChargeList = new ArrayList<>();
		serviceChargeList.add(new ServiceCharge("SBACS000001", "maintaining minimum amount, no detection.", 12000));
		serviceChargeList.add(new ServiceCharge("SBACC000001", "maintaining minimum amount, no detection.", 15000));

		List<ServiceCharge> result = ruleService.getServiceCharges(customerId, token);

		assertThat(serviceChargeList.get(0).getAccountId()).isEqualTo(result.get(0).getAccountId());
	}

}
