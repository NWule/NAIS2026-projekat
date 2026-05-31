package NAIS.PlayerInfoService.service.impl;

import NAIS.PlayerInfoService.dto.ReportDTO;
import NAIS.PlayerInfoService.model.Report;
import NAIS.PlayerInfoService.repository.ReportRepository;
import NAIS.PlayerInfoService.service.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {
    private final ReportRepository reportRepo;

    public Report saveReport(ReportDTO report) {
        Report newReport = new Report();
        newReport.setPlayerId(report.getPlayerId());
        newReport.setScoutId(report.getScoutId());
        newReport.setPsychologicalProfile(report.getPsychologicalProfile());
        newReport.setTacticalNotes(report.getTacticalNotes());
        newReport.setWeaknesses(report.getWeaknesses());
        newReport.setTags(report.getTags());
        newReport.setOverallRating(report.getOverallRating());
        return reportRepo.save(newReport);
    }

    public List<Report> getAllReports() {
        Iterable<Report> iterable = reportRepo.findAll();
        return StreamSupport.stream(iterable.spliterator(), false).toList();
    }

    public Report getReportById(String id) {
        return reportRepo.findById(id).orElse(null);
    }

    public void deleteReport(String id) {
        reportRepo.deleteById(id);
    }

    public Report updateReport(String id, ReportDTO report) {
        Report existingReport = reportRepo.findById(id).orElse(null);
        if (existingReport != null) {
            if (!report.getPlayerId().isEmpty()) {
                existingReport.setPlayerId(report.getPlayerId());
            }
            if (!report.getScoutId().isEmpty()) {
                existingReport.setScoutId(report.getScoutId());
            }
            if (!report.getPsychologicalProfile().isEmpty()) {
                existingReport.setPsychologicalProfile(report.getPsychologicalProfile());
            }
            if (!report.getTacticalNotes().isEmpty()) {
                existingReport.setTacticalNotes(report.getTacticalNotes());
            }
            if (!report.getWeaknesses().isEmpty()) {
                existingReport.setWeaknesses(report.getWeaknesses());
            }
            if (!report.getTags().isEmpty()) {
                existingReport.setTags(report.getTags());
            }
            if (report.getOverallRating() != null) {
                existingReport.setOverallRating(report.getOverallRating());
            }
        }
        return reportRepo.save(existingReport);
    }
}
