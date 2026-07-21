package com.secureflow.paymentservice.kafka;

import com.secureflow.paymentservice.event.PaymentRefundedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentRefundProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(PaymentRefundedEvent event) {

        kafkaTemplate.send(
                "payment-refunded-topic",
                event
        );

        System.out.println(
                "PAYMENT REFUNDED EVENT SENT => "
                        + event
        );
    }
}
