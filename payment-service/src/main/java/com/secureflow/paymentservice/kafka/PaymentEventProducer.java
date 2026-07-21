package com.secureflow.paymentservice.kafka;

import com.secureflow.paymentservice.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(PaymentCompletedEvent event) {

        kafkaTemplate.send(
                "payment-completed-topic",
                event
        );
    }
}
