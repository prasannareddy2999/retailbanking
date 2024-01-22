package com.mfpe.rule.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.mfpe.rule.dto.RuleStatus;
import com.mfpe.rule.dto.ServiceCharge;
import com.mfpe.rule.service.RuleServiceImpl;

@WebMvcTest
class RuleControllerTest {

	@MockBean
	private RuleServiceImpl ruleService;

	@InjectMocks
	private RuleControllerTest ruleControllerTest;

	@Autowired
	private MockMvc mockMvc;

	private RuleStatus ruleStatus;
	private ServiceCharge serviceCharge;
	
	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this); 
	}

	@BeforeEach
	void setUp() {

		ruleStatus = new RuleStatus("Allowed", "Partially OK");
		serviceCharge = new ServiceCharge("SBACS000001", "maintaining minimum amount, no detection", 1000.0);

	}

	@Test
	void testEvaluateMinBal() throws Exception {

		when(this.ruleService.evaluateMinBal(2000.0, "SBACS000001", "validtoken")).thenReturn(ruleStatus);

		this.mockMvc
				.perform(get("/evaluateMinBal/{accountId}/{amount}", "SBACS000001", 2000.0).header("Authorization",
						"validtoken"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.message").value(ruleStatus.getMessage()))
				.andExpect(jsonPath("$.status").value(ruleStatus.getStatus()));

	}

	@Test
	void testGetServiceCharges() throws Exception {
		
		List<ServiceCharge> serviceChargeList = new ArrayList<ServiceCharge>();
		
		serviceChargeList.add(new ServiceCharge("SBACS000001","maintaining minimum amount, no detection.", 12000));
		serviceChargeList.add(new ServiceCharge("SBACC000001","maintaining minimum amount, no detection.", 15000));

		when(this.ruleService.getServiceCharges("SBCU000001", "validtoken")).thenReturn(serviceChargeList);

		this.mockMvc.perform(get("/getServiceCharges/{customerId}", "SBCU000001").header("Authorization", "validtoken"))
				.andExpect(status().isOk());

	}

}
