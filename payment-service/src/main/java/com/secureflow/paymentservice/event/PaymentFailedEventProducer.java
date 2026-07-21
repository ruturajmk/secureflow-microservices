package com.secureflow.paymentservice.event;

import com.secureflow.paymentservice.event.PaymentFailedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentFailedEventProducer {

    private static final Logger log =
            LoggerFactory.getLogger(PaymentFailedEventProducer.class);

    private static final String TOPIC =
            "payment-failed-topic";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PaymentFailedEventProducer(
            KafkaTemplate<String, Object> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPaymentFailedEvent(
            PaymentFailedEvent event) {

        log.info(
                "Publishing PAYMENT FAILED event => orderId={}, username={}, paymentStatus={}, failureReason={}",
                event.getOrderId(),
                event.getUsername(),
                event.getPaymentStatus(),
                event.getFailureReason()
        );

        kafkaTemplate.send(
                TOPIC,
                event
        );

        log.info(
                "PAYMENT FAILED event published => orderId={}",
                event.getOrderId()
        );
    }
}