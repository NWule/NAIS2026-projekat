package com.example.RecommendedActivityService.saga.choreography;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import com.example.RecommendedActivityService.config.RabbitMQConfig;
import com.example.RecommendedActivityService.repository.MatchRepository;
import com.example.RecommendedActivityService.saga.choreography.event.MatchEventDeletionFailedEvent;

@Slf4j
@Component
public class MatchCompensationListener {

    private final MatchRepository matchRepository;
    private final RabbitTemplate rabbitTemplate;

    public MatchCompensationListener(MatchRepository matchRepository, RabbitTemplate rabbitTemplate) {
        this.matchRepository = matchRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.MATCH_EVENTS_DELETION_FAILED_QUEUE)
    public void handleMatchEventDeletionFailed(MatchEventDeletionFailedEvent event) {
        log.warn("[CHOREOGRAPHY][COMPENSATION] Primljen MatchEventDeletionFailedEvent -- sagaId={}, matchId={}, razlog={}",
                event.getSagaId(), event.getMatchId(), event.getReason());

        try {
            matchRepository.rollbackSoftDelete(event.getMatchId());
            log.info("[CHOREOGRAPHY][COMPENSATION] sagaId={} -- Soft delete poništen (rollback uspešan) u Neo4j", event.getSagaId());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.CHOREOGRAPHY_EXCHANGE,
                    RabbitMQConfig.MATCH_COMPENSATED_KEY,
                    event.getSagaId());

        } catch (Exception e) {
            log.error("[CHOREOGRAPHY][COMPENSATION] sagaId={} -- KRITIČNA GREŠKA pri kompenzaciji: {}",
                    event.getSagaId(), e.getMessage(), e);
        }
    }
}