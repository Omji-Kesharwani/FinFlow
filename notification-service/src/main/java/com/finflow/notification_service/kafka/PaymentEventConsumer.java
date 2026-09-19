package com.finflow.notification_service.kafka;


import com.finflow.notification_service.event.PaymentCompletedEvent;

import com.finflow.notification_service.service.NotificationService;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;


@Component
public class PaymentEventConsumer  {
    private final ObjectMapper objectMapper ;
    private final NotificationService notificationService ;

    public PaymentEventConsumer(ObjectMapper objectMapper,NotificationService notificationService)
    {
        this.objectMapper = objectMapper ;
        this.notificationService = notificationService ;
    }


    @KafkaListener(
            topics = "payment-events",
            groupId = "notification-service"
    )
    public void consumePaymentEvent(String message)
    {


        try{

//            System.out.println("RAW KAFKA MESSAGE:");
//            System.out.println(message);

            PaymentCompletedEvent event = objectMapper.readValue(
                    message,
                    PaymentCompletedEvent.class
            );

            System.out.println("========== PAYMENT EVENT RECEIVED ==========");
            System.out.println("Payment ID: " + event.getPaymentId());
            System.out.println("User ID: " + event.getUserId());
            System.out.println("Amount: " + event.getAmount());
            System.out.println("Currency: " + event.getCurrency());
            System.out.println("Reference: " + event.getReference());
            System.out.println("Status: " + event.getStatus());
            System.out.println("============================================");

//            throw new RuntimeException("TEST FAILURE");

            String messageText = String.format(
                    "Payment of %s %s completed successfully. Reference: %s",
                    event.getAmount(),
                    event.getCurrency(),
                    event.getReference()
            );

            notificationService.createPaymentNotification(event.getUserId(),messageText);

        }
        catch (Exception e)
        {
            System.out.println( "Failed to consume payment event: "
                    + e.getMessage());


            throw new RuntimeException(
                    "Failed to process payment event",
                    e
            );


        }
    }

    public void handleDeadLetter(String message)
    {
        System.err.println(
                "========== PAYMENT EVENT MOVED TO DLT =========="
        );

        System.err.println("Message: " + message);

        System.err.println(
                "================================================="
        );
    }
}
