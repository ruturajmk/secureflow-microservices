package com.secureflow.notificationservice.kafka;

import com.secureflow.notificationservice.event.InventoryReservedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryEventConsumer {

    @KafkaListener(
            topics = "inventory-reserved-topic",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(
            InventoryReservedEvent event) {

        System.out.println(
                "EMAIL SENT => Order "
                        + event.orderId()
                        + " confirmed for "
                        + event.username()
        );
    }
}
