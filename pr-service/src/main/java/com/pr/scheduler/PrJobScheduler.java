package com.pr.scheduler;

import com.pr.repository.PrHeaderRepository;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

@Component
public class PrJobScheduler {
    private static final Logger logger = LoggerFactory.getLogger(PrJobScheduler.class);

    private final PrHeaderRepository prHeaderRepository;

    public PrJobScheduler(PrHeaderRepository prHeaderRepository) {
        this.prHeaderRepository = prHeaderRepository;
    }

//    @Scheduled(cron = "0 */5 * * * ?")  // FOR 5 MIN
    @Scheduled(cron = "0 0 0 1 * ?")
    public void auditPurchaseRequests() {

        long totalPrs = prHeaderRepository.count();

        long pendingPrs =
                prHeaderRepository.countByStatus("SUBMITTED");

        logger.info("Monthly PR audit run: totalPRs={}, pendingPRs={}", totalPrs, pendingPrs);

        if (pendingPrs > 0) {
            logger.warn("There are {} Purchase Requests pending approval.", pendingPrs);
        }
    }

}
