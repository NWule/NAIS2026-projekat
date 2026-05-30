package NAIS.PlayerInfoService.service.impl;

import NAIS.PlayerInfoService.repository.ReportRepository;
import NAIS.PlayerInfoService.service.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {
    private final ReportRepository reportRepo;
}
