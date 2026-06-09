package com.pr.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pr.dto.BudgetDTO;

@FeignClient(name = "budget-service", url = "http://localhost:8082")
public interface BudgetFeignClient {

	@GetMapping("/budget/{budgetId}")
	BudgetDTO getBudget(@PathVariable String budgetId);
	
}

