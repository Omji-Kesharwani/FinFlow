package com.finflow.notification_service.kafka;

import com.finflow.notification_service.event.NotificationEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class NotificationEventConsumer {

    private final ObjectMapper objectMapper ;

    public NotificationEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "finflow.notifications",
            groupId = "notification-service"
    )
    public void consume(String message)
    {
      try{
          NotificationEvent event = objectMapper.readValue(message,NotificationEvent.class);
          System.out.println("Notification Event Received");
          System.out.println("User ID: " + event.userId());
          System.out.println("Type: " + event.type());
          System.out.println("Message: " + event.message());
      } catch (Exception e) {
          System.err.println("Failed to process notification event");
          System.err.println("Message: " + message);
          throw new RuntimeException(e);
      }
    }
}
