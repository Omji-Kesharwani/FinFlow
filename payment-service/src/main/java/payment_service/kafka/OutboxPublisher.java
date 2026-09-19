package payment_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import payment_service.entity.OutboxEvent;
import payment_service.repository.OutboxEventRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository ;
    private final KafkaTemplate<String ,String> kafkaTemplate ;

    public OutboxPublisher(OutboxEventRepository outboxEventRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxEventRepository = outboxEventRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 5000)
    public void publishEvents()
    {
        List<OutboxEvent> events = outboxEventRepository.findTop100ByPublishedAtIsNullOrderByCreatedAtAsc() ;
        for(OutboxEvent event : events)
        {
            try{
                kafkaTemplate.send(
                        "payment-events",
                        event.getAggregateId().toString(),
                        event.getPayload()

                ).get() ;
                event.setPublishedAt(OffsetDateTime.now());
                outboxEventRepository.save(event) ;

                System.out.println(
                        "Published outbox event: " + event.getId()
                );
            }
            catch(Exception ex)
            {
                System.err.println(
                        "Failed to publish outbox event"
                        + event.getId()
                        +": "
                        +ex.getMessage()
                );
            }
        }
    }
}
