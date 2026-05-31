package NAIS.PlayerInfoService.controller;

import NAIS.PlayerInfoService.dto.ReportDTO;
import NAIS.PlayerInfoService.model.Report;
import NAIS.PlayerInfoService.service.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final IReportService reportService;

    @GetMapping("/all")
    public List<Report> getAllReports() {
        return reportService.getAllReports();
    }

    @GetMapping("/{id}")
    public Report getReportById(@PathVariable String id) {
        return reportService.getReportById(id);
    }

    @PostMapping("/save")
    public Report saveReport(@RequestBody ReportDTO report) {
        return reportService.saveReport(report);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteReport(@PathVariable String id) {
        reportService.deleteReport(id);
    }

    @PutMapping("/update/{id}")
    public Report updateReport(@PathVariable String id, @RequestBody ReportDTO report) {
        return reportService.updateReport(id, report);
    }

    @GetMapping("/search")
    public List<String> globalReportSearch(@RequestParam(required = false) String textCriteria) {
        return reportService.globalReportSearch(textCriteria);
    }
}
