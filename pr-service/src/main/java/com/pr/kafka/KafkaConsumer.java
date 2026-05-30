package com.pr.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pr.dto.BudgetHeader;

@Service
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    @Autowired
    private ObjectMapper objectMapper;
    


    @KafkaListener(topics = "pr-created", groupId = "pr-service-group")
    public void listen(String message) {
        logger.info("[pr-service] Received kafka message on pr-created: {}", message);
    }
    
    @KafkaListener(topics = "budget-created", groupId = "pr-service-group")
    public void listenBudgetCreated(String message) throws Exception {

        BudgetHeader budget =
        		objectMapper.readValue(message, BudgetHeader.class);

        logger.info("Budget Id : {}", budget.getBudgetId());

        logger.info("Available Amount : {}", budget.getAvailableAmount());

        logger.info("Status : {}", budget.getStatus());
    }
}
