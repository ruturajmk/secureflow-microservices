package com.secureflow.inventoryservice.kafka;

import com.secureflow.inventoryservice.event.InventoryReservedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(
            InventoryReservedEvent event) {

        kafkaTemplate.send(
                "inventory-reserved-topic",
                event
        );

        System.out.println(
                "INVENTORY RESERVED EVENT SENT => "
                        + event
        );
    }
}