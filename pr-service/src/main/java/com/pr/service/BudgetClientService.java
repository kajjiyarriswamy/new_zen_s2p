package com.pr.service;

import com.pr.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

		System.out.println("Calling Budget Service" + budgetId);

		String url = "http://localhost:8082/budget/" + budgetId;

		ResponseEntity<ApiResponse<BudgetDTO>> response =
				restTemplate.exchange(
						url,
						HttpMethod.GET,
						null,
						new ParameterizedTypeReference<ApiResponse<BudgetDTO>>() {}
				);

		return response.getBody().getData();
	}

	public BudgetDTO fallbackBudget(String budgetId, Exception ex) {

		System.out.println("Fallback executed");

		return null;
	}
}
