package com.budget.scheduler;

import com.budget.repository.BudgetHeaderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BudgetDocumentAuditScheduler {

    private static final Logger logger = LoggerFactory.getLogger(BudgetDocumentAuditScheduler.class);
    private final BudgetHeaderRepository budgetHeaderRepository;

    public BudgetDocumentAuditScheduler(BudgetHeaderRepository budgetHeaderRepository) {
        this.budgetHeaderRepository = budgetHeaderRepository;
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void auditBudgetDocumentPaths() {
        long missingDocuments = budgetHeaderRepository.countByDocumentPathIsNull();
        long totalBudgets = budgetHeaderRepository.count();
        logger.info("Budget document audit run: totalBudgets={}, missingDocumentPathCount={}", totalBudgets, missingDocuments);

        if (missingDocuments > 0) {
            logger.warn("There are {} budgets without S3 document path configured.", missingDocuments);
        }
    }
}
