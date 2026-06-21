package NAIS.PlayerRecommendationService.saga;

import NAIS.PlayerRecommendationService.config.RabbitMQConfig;
import NAIS.PlayerRecommendationService.models.nodes.Metric;
import NAIS.PlayerRecommendationService.models.nodes.Player;
import NAIS.PlayerRecommendationService.models.nodes.Report;
import NAIS.PlayerRecommendationService.models.nodes.Scout;
import NAIS.PlayerRecommendationService.models.relationships.Valued;
import NAIS.PlayerRecommendationService.repository.MetricRepo;
import NAIS.PlayerRecommendationService.repository.PlayerRepo;
import NAIS.PlayerRecommendationService.repository.ReportRepo;
import NAIS.PlayerRecommendationService.repository.ScoutRepo;
import NAIS.PlayerRecommendationService.saga.event.ReportCreatedEvent;
import NAIS.PlayerRecommendationService.saga.event.ReportCreationConfirmationEvent;
import NAIS.PlayerRecommendationService.saga.event.ReportCreationFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportCreationListener {
    private final ReportRepo reportRepo;
    private final PlayerRepo playerRepo;
    private final ScoutRepo scoutRepo;
    private final MetricRepo metricRepo;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.REPORT_CREATED_QUEUE)
    public void handleReportCreated(ReportCreatedEvent event) {
        try {
            if (reportRepo.existsById(event.getReportId())) {
                log.info("Report already exists, skipping creation: {}", event.getReportId());
                return;
            }
            log.info("Processing ReportCreatedEvent: {}", event);

            Player player = playerRepo.findById(event.getPlayerId()).orElseThrow();
            Scout scout = scoutRepo.findById(event.getScoutId()).orElseThrow();

            Report report = new Report();
            report.setId(event.getReportId());
            report.setText(event.getText());
            report.setScore(event.getScore());
            report.setGame(event.getGame());
            report.setPlayer(player);

            List<Valued> valuedMetrics = new ArrayList<>();
            event.getMetricValues().forEach((metricId, value) -> {
                Metric metric = metricRepo.findById(metricId).orElseThrow();

                Valued valued = new Valued();
                valued.setMetric(metric);
                valued.setScore(value);
                valued.setCreationDate(LocalDate.now());

                valuedMetrics.add(valued);
            });
            report.setMetrics(valuedMetrics);

            Report savedReport = reportRepo.save(report);

            scout.getReports().add(savedReport);
            scoutRepo.save(scout);

            reportRepo.save(report);
        } catch (Exception e) {
            ReportCreationFailedEvent failedEvent = new ReportCreationFailedEvent(event.getReportId(), e.getMessage());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.REPORT_CREATED_CONFIRMATION_QUEUE,
                    RabbitMQConfig.REPORT_CREATED_FAILED_KEY,
                    failedEvent
            );

            log.error("Error processing ReportCreatedEvent: {}", e.getMessage());
        }

        ReportCreationConfirmationEvent confirmationEvent = new ReportCreationConfirmationEvent(event.getReportId());

        try {
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.REPORT_CREATED_CONFIRMATION_QUEUE,
                RabbitMQConfig.REPORT_CREATED_PROCESSED_KEY,
                confirmationEvent
            );
        } catch (Exception e) {
            log.error("Error sending ReportCreationConfirmationEvent: {}", e.getMessage());
        }
    }
}
