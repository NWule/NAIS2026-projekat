package NAIS.PlayerInfoService.saga;

import NAIS.PlayerInfoService.config.RabbitMQConfig;
import NAIS.PlayerInfoService.dto.SAGACreateReportDto;
import NAIS.PlayerInfoService.model.Report;
import NAIS.PlayerInfoService.repository.ReportRepository;
import NAIS.PlayerInfoService.saga.event.ReportCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportCreationService {
    private final ReportRepository reportRepo;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public Report createReport(SAGACreateReportDto dto) {
        Report newReport = new Report();
        try {
            newReport = new Report();
            newReport.setPlayerId(dto.getPlayerId());
            newReport.setScoutId(dto.getScoutId());
            newReport.setPsychologicalProfile(dto.getPsychologicalProfile());
            newReport.setTacticalNotes(dto.getTacticalNotes());
            newReport.setWeaknesses(dto.getWeaknesses());
            newReport.setTags(dto.getTags());
            newReport.setOverallRating(dto.getOverallRating());

            newReport = reportRepo.save(newReport);
        } catch (Exception e) {
            throw new RuntimeException("Greska prilikom cuvanja novog izvestaja.", e);
        }

        ReportCreatedEvent event = new ReportCreatedEvent(
                newReport.getReportId(),
                dto.getTacticalNotes(),
                dto.getOverallRating(),
                dto.getGame(),
                dto.getScoutId(),
                dto.getPlayerId(),
                dto.getMetricValues()
        );

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.REPORT_CREATED_KEY,
                    event
            );
        } catch (Exception e) {
            reportRepo.deleteById(newReport.getReportId());
            throw new RuntimeException("Greska prilikom slanja event-a.", e);
        }

        return newReport;
    }
}
