package com.invoice.async;

import com.invoice.dto.CreateInvoiceResponseDTO;
import com.invoice.kafka.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class InvoiceAsyncService {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceAsyncService.class);
    private final KafkaProducer kafkaProducer;

    public InvoiceAsyncService(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @Async("invoiceTaskExecutor")
    public void publishInvoiceCreatedEvent(CreateInvoiceResponseDTO invoiceResponse) {
        try {
            kafkaProducer.send("invoice-created", invoiceResponse);
            logger.info("Published invoice-created event asynchronously for invoice {}", invoiceResponse.getInvoiceNumber());
        } catch (Exception ex) {
            logger.error("Failed to publish invoice-created event asynchronously", ex);
        }
    }

    @Async("invoiceTaskExecutor")
    public void auditInvoiceUpload(String invoiceNumber, String documentPath) {
        logger.info("Asynchronously auditing upload for invoice {} and document {}", invoiceNumber, documentPath);
    }
}
