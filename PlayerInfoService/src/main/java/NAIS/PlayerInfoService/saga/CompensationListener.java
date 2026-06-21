package NAIS.PlayerInfoService.saga;

import NAIS.PlayerInfoService.config.RabbitMQConfig;
import NAIS.PlayerInfoService.repository.ReportRepository;
import NAIS.PlayerInfoService.saga.event.ReportCreationConfirmationEvent;
import NAIS.PlayerInfoService.saga.event.ReportCreationFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@RabbitListener(queues = RabbitMQConfig.REPORT_CREATED_CONFIRMATION_QUEUE)
public class CompensationListener {
    private final ReportRepository reportRepo;

    @RabbitHandler
    public void handleFailure(ReportCreationFailedEvent failedEvent) {
        try {
            if (reportRepo.existsById(failedEvent.getReportId())) {
                reportRepo.deleteById(failedEvent.getReportId());
                log.warn("Report creation failed for report ID: " + failedEvent.getReportId() + ", report deleted.");
            } else {
                log.warn("Report creation failed for report ID: " + failedEvent.getReportId() + ", report not found.");
            }
        } catch (Exception e) {
            log.error("Error while trying to delete report: " + e.getMessage(), e);
        }
    }

    @RabbitHandler
    public void handleConfirmation(ReportCreationConfirmationEvent confirmationEvent) {
        log.info("Report creation confirmed for report ID: " + confirmationEvent.getReportId());
    }

    @RabbitHandler(isDefault = true)
    public void handleUnknown(Object event) {
        log.warn("Unknown event type: " + event.getClass().getName());
    }
}
