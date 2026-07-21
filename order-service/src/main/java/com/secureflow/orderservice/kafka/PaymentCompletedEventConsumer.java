package com.secureflow.orderservice.kafka;

import com.secureflow.orderservice.entity.Order;
import com.secureflow.orderservice.event.PaymentCompletedEvent;
import com.secureflow.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentCompletedEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(PaymentCompletedEventConsumer.class);

    private final OrderRepository orderRepository;

    public PaymentCompletedEventConsumer(
            OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(
            topics = "payment-completed-topic",
            groupId = "order-service-payment-group",
            containerFactory = "paymentCompletedKafkaListenerContainerFactory"
    )
    public void consume(PaymentCompletedEvent event) {

        log.info(
                "PAYMENT COMPLETED EVENT RECEIVED => orderId={}, username={}, status={}",
                event.getOrderId(),
                event.getUsername(),
                event.getPaymentStatus()
        );

        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Order not found: " + event.getOrderId()
                        )
                );

        order.setStatus("PAYMENT_COMPLETED");

        orderRepository.save(order);

        log.info(
                "ORDER STATUS UPDATED => orderId={}, status={}",
                order.getId(),
                order.getStatus()
        );
    }
}