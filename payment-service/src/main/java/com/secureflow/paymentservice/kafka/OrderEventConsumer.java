package com.secureflow.paymentservice.kafka;

import com.secureflow.paymentservice.event.OrderCreatedEvent;
import com.secureflow.paymentservice.event.PaymentCompletedEvent;
import com.secureflow.paymentservice.event.PaymentFailedEvent;
import com.secureflow.paymentservice.event.PaymentFailedEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final PaymentEventProducer paymentEventProducer;
    private final PaymentFailedEventProducer paymentFailedEventProducer;

    @KafkaListener(
            topics = "order-created-topic",
            groupId = "payment-group"
    )
    public void consume(OrderCreatedEvent event) {

        System.out.println(
                "PAYMENT PROCESSING => orderId="
                        + event.orderId()
                        + ", username="
                        + event.username()
        );

        try {

            /*
             * =====================================================
             * PAYMENT PROCESSING LOGIC
             * =====================================================
             *
             * Currently we don't have a real payment gateway.
             * Therefore payment is considered successful.
             *
             * Later we can replace this with actual payment
             * processing logic.
             */
            boolean paymentSuccessful = false;

            if (paymentSuccessful) {

                PaymentCompletedEvent completedEvent =
                        new PaymentCompletedEvent(
                                event.orderId(),
                                event.username(),
                                "SUCCESS"
                        );

                paymentEventProducer.publish(
                        completedEvent
                );

                System.out.println(
                        "PAYMENT COMPLETED EVENT SENT => orderId="
                                + event.orderId()
                );

            } else {

                PaymentFailedEvent failedEvent =
                        new PaymentFailedEvent(
                                event.orderId(),
                                event.username(),
                                "FAILED",
                                "Payment processing failed"
                        );

                paymentFailedEventProducer.publishPaymentFailedEvent(
                        failedEvent
                );

                System.out.println(
                        "PAYMENT FAILED EVENT SENT => orderId="
                                + event.orderId()
                );
            }

        } catch (Exception exception) {

            System.err.println(
                    "PAYMENT PROCESSING ERROR => orderId="
                            + event.orderId()
                            + ", error="
                            + exception.getMessage()
            );

            PaymentFailedEvent failedEvent =
                    new PaymentFailedEvent(
                            event.orderId(),
                            event.username(),
                            "FAILED",
                            exception.getMessage() != null
                                    ? exception.getMessage()
                                    : "Unexpected payment processing error"
                    );

            paymentFailedEventProducer.publishPaymentFailedEvent(
                    failedEvent
            );
        }
    }
}
