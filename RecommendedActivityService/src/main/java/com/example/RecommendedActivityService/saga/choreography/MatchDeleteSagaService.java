package com.example.RecommendedActivityService.saga.choreography;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.example.RecommendedActivityService.config.RabbitMQConfig;
import com.example.RecommendedActivityService.repository.MatchRepository;
import com.example.RecommendedActivityService.saga.choreography.event.MatchEventDeletionFailedEvent;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class MatchDeleteSagaService {

    private final MatchRepository matchRepository;
    private final RabbitTemplate rabbitTemplate;

    public MatchDeleteSagaService(MatchRepository matchRepository, RabbitTemplate rabbitTemplate) {
        this.matchRepository = matchRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public String initiateMatchDeletion(Long matchId) {
        String sagaId = UUID.randomUUID().toString();
        log.info("[CHOREOGRAPHY] Pokrećem brisanje meča sagaId={} -- matchId={}", sagaId, matchId);

        try {
            matchRepository.softDeleteMatchAndPerformances(matchId);
            log.info("[CHOREOGRAPHY] sagaId={} -- Meč i relacije markirani kao obrisani u Neo4j", sagaId);

        } catch (Exception e) {
            log.error("[CHOREOGRAPHY] sagaId={} -- GREŠKA pri pisanju u Neo4j: {}", sagaId, e.getMessage(), e);
            throw new RuntimeException("Neo4j operacija nije uspela za sagaId=" + sagaId, e);
        }

        MatchDeletedEvent event = new MatchDeletedEvent(sagaId, matchId, LocalDateTime.now());

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.CHOREOGRAPHY_EXCHANGE,
                    RabbitMQConfig.MATCH_DELETED_KEY,
                    event);
            log.info("[CHOREOGRAPHY] sagaId={} -- MatchDeletedEvent emitovan", sagaId);
        } catch (Exception e) {
            log.error("[CHOREOGRAPHY] sagaId={} -- GREŠKA pri emitovanju na RabbitMQ: {}", sagaId, e.getMessage(), e);
            log.warn("[CHOREOGRAPHY] sagaId={} -- Pokrećem trenutnu kompenzaciju zbog pada brokera", sagaId);
            matchRepository.rollbackSoftDelete(matchId);
            throw new RuntimeException("RabbitMQ emitovanje nije uspelo za sagaId=" + sagaId, e);
        }

        return sagaId;
    }
}