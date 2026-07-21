package com.secureflow.paymentservice.kafka;

import com.secureflow.paymentservice.event.InventoryFailedEvent;
import com.secureflow.paymentservice.event.PaymentRefundedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryFailureConsumer {

    private final PaymentRefundProducer producer;

    @KafkaListener(
            topics = "inventory-failed-topic",
            groupId = "payment-group",
            containerFactory = "inventoryFailedKafkaListenerContainerFactory"
    )
    public void consume(
            InventoryFailedEvent event) {

        System.out.println(
                "REFUND INITIATED => "
                        + event.orderId()
        );

        producer.publish(
                new PaymentRefundedEvent(
                        event.orderId(),
                        event.username(),
                        "REFUNDED"
                )
        );
    }
}
