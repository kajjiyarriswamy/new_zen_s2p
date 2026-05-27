package com.user.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "user-created", groupId = "user-service-group")
    public void listen(String message) {
        logger.info("[user-service] Received kafka message on user-created: {}", message);
    }
}
