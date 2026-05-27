package com.po.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "po-created", groupId = "po-service-group")
    public void listen(String message) {
        logger.info("[po-service] Received kafka message on po-created: {}", message);
    }
}
