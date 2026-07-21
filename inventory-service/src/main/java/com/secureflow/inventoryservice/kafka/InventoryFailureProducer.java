package com.secureflow.inventoryservice.kafka;

import com.secureflow.inventoryservice.event.InventoryFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryFailureProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(InventoryFailedEvent event) {

        kafkaTemplate.send(
                "inventory-failed-topic",
                event
        );

        System.out.println(
                "INVENTORY FAILED EVENT SENT => "
                        + event
        );
    }
}