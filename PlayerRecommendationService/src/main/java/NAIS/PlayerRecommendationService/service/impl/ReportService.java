package NAIS.PlayerRecommendationService.service.impl;

import NAIS.PlayerRecommendationService.dto.CreateReportDto;
import NAIS.PlayerRecommendationService.dto.UpdateReportDto;
import NAIS.PlayerRecommendationService.models.nodes.Metric;
import NAIS.PlayerRecommendationService.models.nodes.Player;
import NAIS.PlayerRecommendationService.models.nodes.Report;
import NAIS.PlayerRecommendationService.models.nodes.Scout;
import NAIS.PlayerRecommendationService.models.relationships.Valued;
import NAIS.PlayerRecommendationService.repository.MetricRepo;
import NAIS.PlayerRecommendationService.repository.PlayerRepo;
import NAIS.PlayerRecommendationService.repository.ReportRepo;
import NAIS.PlayerRecommendationService.repository.ScoutRepo;
import NAIS.PlayerRecommendationService.service.IReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService implements IReportService {

    private final ReportRepo reportRepo;
    private final PlayerRepo playerRepo;
    private final ScoutRepo scoutRepo;
    private final MetricRepo metricRepo;

    public ReportService(ReportRepo reportRepo, PlayerRepo playerRepo, ScoutRepo scoutRepo, MetricRepo metricRepo) {
        this.reportRepo = reportRepo;
        this.playerRepo = playerRepo;
        this.scoutRepo = scoutRepo;
        this.metricRepo = metricRepo;
    }

    @Override
    public List<Report> getAllReports() {
        return reportRepo.findAll();
    }

    public Report getReportById(Long id) {
        return reportRepo.findById(id).orElse(null);
    }

    public Report updateReport(Long id, UpdateReportDto dto) {
        Report reportToUpdate = reportRepo.findById(id).orElse(null);
        if (reportToUpdate != null) {
            reportToUpdate.setText(dto.getText());
            reportToUpdate.setScore(dto.getScore());
            reportToUpdate.setGame(dto.getGame());
            return reportRepo.save(reportToUpdate);
        }
        return null;
    }

    @Transactional
    public Report saveReport(CreateReportDto dto) {
        Player player = playerRepo.findById(dto.getPlayerId()).orElseThrow();
        Scout scout = scoutRepo.findById(dto.getScoutId()).orElseThrow();

        Report report = new Report();
        report.setText(dto.getText());
        report.setScore(dto.getScore());
        report.setGame(dto.getGame());
        report.setPlayer(player);

        List<Valued> valuedMetrics = new ArrayList<>();
        dto.getMetricValues().forEach((metricId, value) -> {
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

        return savedReport;
    }

    public boolean deleteReport(Long id) {
        if (reportRepo.findById(id).isEmpty()) {
            return false;
        }
        reportRepo.deleteById(id);
        return true;
    }

    public void updateMetricScore(Long reportId, Long metricId, Integer newScore) {
        Integer updatedCount = reportRepo.updateMetricScore(reportId, metricId, newScore);

        if (updatedCount == 0) {
            throw new RuntimeException("Could not find a 'VALUED' relationship between " +
                    "Report ID " + reportId + " and Metric ID " + metricId);
        }
    }
}
