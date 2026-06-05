package com.budget.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.budget.dto.PrData;
import com.budget.service.BudgetService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    
    @Autowired
    BudgetService budgetService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @KafkaListener(topics = "budget-created", groupId = "budget-service-group")
    public void listen(String message) {
        logger.info("[budget-service] Received kafka message on budget-created: {}", message);
    }
    
    @KafkaListener(topics = "pr-approved", groupId = "budget-service-group")
    public void listenPrApproved(String message) throws Exception {
    	PrData event =
                objectMapper.readValue(message, PrData.class);

        String id = event.getId();
        Double budget = event.getBudget();
        budgetService.updateBudgetAmount(event);
        logger.info("ID: {}, Budget: {}", id, budget);
    }
    
    
    @KafkaListener(topics = "pr-rejected", groupId = "budget-service-group")
    public void listenPrRejected(String message) throws Exception {
    	PrData event =
                objectMapper.readValue(message, PrData.class);

        String id = event.getId();
        Double budget = event.getBudget();
        budgetService.updateBudgetAmountforPrRejected(event);
        logger.info("ID: {}, Budget: {}", id, budget);
    }

}
