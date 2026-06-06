package com.pr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pr.dto.ApiResponse;
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

	    String url = "http://localhost:8083/budget/" + budgetId;

	    ResponseEntity<ApiResponse<BudgetDTO>> response =
	            restTemplate.exchange(
	                    url,
	                    org.springframework.http.HttpMethod.GET,
	                    null,
	                    new ParameterizedTypeReference<ApiResponse<BudgetDTO>>() {});

	    ApiResponse<BudgetDTO> apiResponse = response.getBody();

	    System.out.println("Status = " + apiResponse.getStatusCode());
	    System.out.println("Message = " + apiResponse.getMessage());

	    BudgetDTO dto = apiResponse.getData();

	    System.out.println("Budget Id = " + dto.getBudgetId());
	    System.out.println("Budget Description = " + dto.getBudgetDescription());

	    return dto;
	}
	public BudgetDTO fallbackBudget(String budgetId, Exception ex) {

		System.out.println("Fallback executed");

		return null;
	}
}
