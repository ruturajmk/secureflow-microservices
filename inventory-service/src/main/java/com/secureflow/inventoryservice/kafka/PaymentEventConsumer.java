package com.secureflow.inventoryservice.kafka;

import com.secureflow.inventoryservice.event.InventoryFailedEvent;
import com.secureflow.inventoryservice.event.InventoryReservedEvent;
import com.secureflow.inventoryservice.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventConsumer {

    // Toggle this flag
    @Value("${inventory.available}")
    private boolean inventoryAvailable;

    private final InventoryEventProducer producer;
    private final InventoryFailureProducer failureProducer;

    @KafkaListener(
            topics = "payment-completed-topic",
            groupId = "inventory-group"
    )
    public void consume(
            PaymentCompletedEvent event) {

        if (inventoryAvailable) {

            System.out.println(
                    "INVENTORY RESERVATION STARTED => "
                            + event
            );

            producer.publish(
                    new InventoryReservedEvent(
                            event.orderId(),
                            event.username(),
                            "RESERVED"
                    )
            );

        } else {

            System.out.println(
                    "INVENTORY FAILED => "
                            + event.orderId()
            );

            failureProducer.publish(
                    new InventoryFailedEvent(
                            event.orderId(),
                            event.username(),
                            "Inventory unavailable"
                    )
            );
        }
    }
}
