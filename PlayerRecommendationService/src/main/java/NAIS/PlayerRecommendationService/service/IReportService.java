package NAIS.PlayerRecommendationService.service;

import NAIS.PlayerRecommendationService.dto.CreateReportDto;
import NAIS.PlayerRecommendationService.dto.UpdateReportDto;
import NAIS.PlayerRecommendationService.models.nodes.Report;

import java.util.List;

public interface IReportService {
    public List<Report> getAllReports();
    public Report saveReport(CreateReportDto report);
    public Report getReportById(Long id);
    public Report updateReport(Long id, UpdateReportDto report);
    public boolean deleteReport(Long id);
    public void updateMetricScore(Long reportId, Long metricId, Integer newScore);
}
