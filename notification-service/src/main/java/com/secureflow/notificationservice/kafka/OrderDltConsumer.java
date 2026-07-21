package com.secureflow.notificationservice.kafka;

import com.secureflow.notificationservice.event.OrderCreatedEvent;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.stereotype.Service;

@Service
public class OrderDltConsumer {

    @DltHandler
    public void processDlt(OrderCreatedEvent event) {

        System.out.println(
                "DLQ RECEIVED => " + event
        );
    }
}
