package NAIS.PlayerRecommendationService.controller;

import NAIS.PlayerRecommendationService.dto.CreateReportDto;
import NAIS.PlayerRecommendationService.dto.UpdateReportDto;
import NAIS.PlayerRecommendationService.models.nodes.Report;
import NAIS.PlayerRecommendationService.service.impl.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("")
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable String id) {
        Report report = reportService.getReportById(id);
        if (report != null) {
            return ResponseEntity.ok(report);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("")
    public ResponseEntity<Report> updateReport(@RequestParam String id, @RequestBody UpdateReportDto updatedReport) {
        Report report = reportService.updateReport(id, updatedReport);
        if (report != null) {
            return ResponseEntity.ok(report);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteReport(@RequestParam String id) {
        if (reportService.deleteReport(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{reportId}/metrics/{metricId}")
    public ResponseEntity<Void> updateMetricScore(
            @PathVariable String reportId,
            @PathVariable String metricId,
            @RequestParam Integer score) {

        reportService.updateMetricScore(reportId, metricId, score);
        return ResponseEntity.ok().build();
    }
}
