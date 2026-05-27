package com.pr.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "pr-created", groupId = "pr-service-group")
    public void listen(String message) {
        logger.info("[pr-service] Received kafka message on pr-created: {}", message);
    }
}
