package NAIS.PlayerInfoService.service;

import NAIS.PlayerInfoService.dto.ReportDTO;
import NAIS.PlayerInfoService.model.Report;
import NAIS.PlayerInfoService.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface IReportService {
    List<Report> getAllReports();
    Report getReportById(String id);
    Report saveReport(ReportDTO report);
    void deleteReport(String id);
    Report updateReport(String id, ReportDTO report);
}
