package com.pr.service;

import com.pr.dto.PrData;
import com.pr.kafka.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PrAsyncService {

    private static final Logger logger = LoggerFactory.getLogger(PrAsyncService.class);

    private final KafkaProducer kafkaProducer;

    public PrAsyncService(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @Async("prTaskExecutor")
    public void publishPrCreatedEvent(PrData prData) {

        try {
            logger.info("Publishing PR event on thread {}", Thread.currentThread().getName());
            kafkaProducer.send("pr-created", prData);
            logger.info("Published PR event for {}", prData.getPrNumber());

        } catch (Exception e) {

            logger.error("Failed to publish PR event", e);
        }
    }
}