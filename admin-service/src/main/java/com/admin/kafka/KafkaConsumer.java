package com.admin.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "org-created", groupId = "admin-service-group")
    public void listen(String message) {
        logger.info("[admin-service] Received kafka message on org-created: {}", message);
    }
}
