package com.pr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pr.dto.Budget1Dto;
import com.pr.dto.BudgetApiResponse;
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

		BudgetApiResponse response= restTemplate.getForObject(url, BudgetApiResponse.class);
		return response !=null ? response.getData() : null;
	}

	public BudgetDTO fallbackBudget(String budgetId, Exception ex) {

		System.out.println("Fallback executed");

		return null;
	}

	@CircuitBreaker(name= "budgetService", fallbackMethod = "fallbackBudget1")
	@Retry(name= "budgetService")
	public Budget1Dto getBudget1(String budgetId) {
		
		System.out.println("Calling Budget Service");

		String url = "http://localhost:8082/budget/" + budgetId;

		return restTemplate.getForObject(url, Budget1Dto.class);
	}
	
	public Budget1Dto fallbackBudget1(String budgetId, Exception ex) {
		System.out.println("Fallback executed");
		
		return null;
	}
}
