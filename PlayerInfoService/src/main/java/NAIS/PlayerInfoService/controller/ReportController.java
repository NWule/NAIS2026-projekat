package NAIS.PlayerInfoService.controller;

import NAIS.PlayerInfoService.dto.PlayerTierResponse;
import NAIS.PlayerInfoService.dto.ReportDTO;
import NAIS.PlayerInfoService.dto.TacticalTrendResponse;
import NAIS.PlayerInfoService.model.Report;
import NAIS.PlayerInfoService.service.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final IReportService reportService;

    @GetMapping("/all")
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable String id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<Report> saveReport(@RequestBody ReportDTO report) {
        return ResponseEntity.ok(reportService.saveReport(report));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Report> deleteReport(@PathVariable String id) {
        reportService.deleteReport(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Report> updateReport(@PathVariable String id, @RequestBody ReportDTO report) {
        return ResponseEntity.ok(reportService.updateReport(id, report));
    }

    @GetMapping("/search")
    public ResponseEntity<List<String>> globalReportSearch(@RequestParam(required = false) String textCriteria) {
        return ResponseEntity.ok(reportService.globalReportSearch(textCriteria));
    }

    @GetMapping("/tactical-trends")
    public ResponseEntity<TacticalTrendResponse> getTacticalTrends(
            @RequestParam String tacticalSearch,
            @RequestParam String requiredTag) {

        TacticalTrendResponse response = reportService.analyzeTacticalTrends(tacticalSearch, requiredTag);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/player-tiers")
    public ResponseEntity<PlayerTierResponse> getPlayerTiers(
            @RequestParam String searchTerms) {

        PlayerTierResponse response = reportService.analyzePlayerTiers(searchTerms);
        return ResponseEntity.ok(response);
    }
}
