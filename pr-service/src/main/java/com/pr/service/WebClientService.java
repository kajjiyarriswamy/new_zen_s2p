package com.pr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.pr.dto.BudgetDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class WebClientService {

	@Autowired
	private WebClient webClient;

	@CircuitBreaker(name = "budgetService", fallbackMethod = "fallbackBudget")
	@Retry(name = "budgetService")
	public BudgetDTO getBudget(String budgetId) {

		return webClient.get().uri("http://localhost:8082/budgets/" + budgetId).retrieve().bodyToMono(BudgetDTO.class)
				.block();
	}

	public BudgetDTO fallbackBudget(Long budgetId, Exception ex) {

		System.out.println("Fallback executed");

		return null;
	}

}
