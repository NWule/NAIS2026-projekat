package NAIS.PlayerRecommendationService.service;

import NAIS.PlayerRecommendationService.dto.CreateReportDto;
import NAIS.PlayerRecommendationService.dto.UpdateReportDto;
import NAIS.PlayerRecommendationService.models.nodes.Report;

import java.util.List;

public interface IReportService {
    public List<Report> getAllReports();
    public Report saveReport(CreateReportDto report);
    public Report getReportById(String id);
    public Report updateReport(String id, UpdateReportDto report);
    public boolean deleteReport(String id);
    public void updateMetricScore(String reportId, String metricId, Integer newScore);
}
