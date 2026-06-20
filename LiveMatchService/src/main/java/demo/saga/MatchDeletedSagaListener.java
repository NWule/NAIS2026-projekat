package demo.saga;

import demo.configuration.RabbitMQConfig;
import demo.repository.MatchEventRepository;
import demo.saga.event.MatchDeletedEvent;
import demo.saga.event.MatchEventDeletionFailedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MatchDeletedSagaListener {

    private static final Logger log = LoggerFactory.getLogger(MatchDeletedSagaListener.class);

    private final MatchEventRepository matchEventRepository;
    private final RabbitTemplate rabbitTemplate;

    public MatchDeletedSagaListener(MatchEventRepository matchEventRepository, RabbitTemplate rabbitTemplate) {
        this.matchEventRepository = matchEventRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.MATCH_DELETED_QUEUE)
    public void handleMatchDeletedCommand(MatchDeletedEvent event) {
        log.info("[CHOREOGRAPHY] Primljen zahtev za brisanje događaja meča sagaId={}, matchId={}",
                event.getSagaId(), event.getMatchId());

        try {
            String matchIdStr = String.valueOf(event.getMatchId());
            Boolean success = matchEventRepository.markMatchAsDeleted(matchIdStr);

            if (!success) {
                throw new RuntimeException("InfluxDB save operacija vratila false");
            }

            log.info("[CHOREOGRAPHY] sagaId={} -- Uspešno postavljen Tombstone marker za meč u InfluxDB", event.getSagaId());

        } catch (Exception e) {
            log.error("[CHOREOGRAPHY] sagaId={} -- GREŠKA pri radu sa InfluxDB. Pokrećem kompenzaciju: {}",
                    event.getSagaId(), e.getMessage());

            MatchEventDeletionFailedEvent failureEvent = new MatchEventDeletionFailedEvent(
                    event.getSagaId(),
                    event.getMatchId(),
                    "INFLUX_DB_ERROR: " + e.getMessage()
            );

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.CHOREOGRAPHY_EXCHANGE,
                    RabbitMQConfig.MATCH_EVENTS_DELETION_FAILED_KEY,
                    failureEvent
            );

            log.info("[CHOREOGRAPHY] sagaId={} -- MatchEventDeletionFailedEvent emitovan za kompenzaciju", event.getSagaId());
        }
    }
}