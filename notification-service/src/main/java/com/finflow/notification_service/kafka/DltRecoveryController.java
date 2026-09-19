package com.finflow.notification_service.kafka;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dlt")
public class DltRecoveryController {

    private final KafkaTemplate<String,String> kafkaTemplate ;

    public DltRecoveryController(
            KafkaTemplate<String,String> kafkaTemplate
    ){
        this.kafkaTemplate = kafkaTemplate ;
    }

    @PostMapping("/replay")
    public ResponseEntity<String>replayEvent(@RequestBody String message)
    {
        kafkaTemplate.send(
                "payment-events",
                message
        );

        return ResponseEntity.ok(
                "Payment event replayed to payment-events"
        );
    }
}
