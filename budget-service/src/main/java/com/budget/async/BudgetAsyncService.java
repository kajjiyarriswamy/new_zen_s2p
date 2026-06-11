package com.budget.async;

import com.budget.dto.BudgetHeader;
import com.budget.kafka.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class BudgetAsyncService {

    private static final Logger logger = LoggerFactory.getLogger(BudgetAsyncService.class);
    private final KafkaProducer kafkaProducer;

    public BudgetAsyncService(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @Async("budgetTaskExecutor")
    public void publishBudgetCreatedEvent(BudgetHeader budgetHeader) {
        try {
            kafkaProducer.send("budget-created", budgetHeader);
            logger.info("Published budget-created event asynchronously for budget {}", budgetHeader.getBudgetId());
        } catch (Exception ex) {
            logger.error("Failed to publish budget-created event asynchronously", ex);
        }
    }

    @Async("budgetTaskExecutor")
    public void auditBudgetUpload(String budgetId, String documentPath) {
        logger.info("Asynchronously auditing upload for budget {} and document {}", budgetId, documentPath);
    }
}
