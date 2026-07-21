package com.secureflow.orderservice.kafka;

import com.secureflow.orderservice.event.PaymentRefundedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentRefundConsumer {

    @KafkaListener(
            topics = "payment-refunded-topic",
            groupId = "order-group"
    )
    public void consume(PaymentRefundedEvent event) {

        System.out.println("ORDER CANCELLED => " + event.getOrderId());

        System.out.println(event);
    }
}
