package com.pr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pr.dto.BudgetDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class BudgetClientService {

	@Autowired
	private RestTemplate restTemplate;

	@CircuitBreaker(name = "budgetService", fallbackMethod = "fallbackBudget")
	@Retry(name = "budgetService")
	public BudgetDTO getBudget(String budgetId) {

		System.out.println("Calling Budget Service");

		String url = "http://localhost:8082/budget/" + budgetId;

		return restTemplate.getForObject(url, BudgetDTO.class);
	}

	public BudgetDTO fallbackBudget(String budgetId, Exception ex) {

		System.out.println("Fallback executed");

		return null;
	}
}
