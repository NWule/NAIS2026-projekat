package NAIS.PlayerInfoService.service.impl;

import NAIS.PlayerInfoService.dto.ReportDTO;
import NAIS.PlayerInfoService.model.Report;
import NAIS.PlayerInfoService.repository.ReportRepository;
import NAIS.PlayerInfoService.service.IReportService;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {
    private final ReportRepository reportRepo;
    private final ElasticsearchTemplate elasticsearchTemplate;

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

    public List<String> globalReportSearch(String textCriteria) {

        if (textCriteria == null || textCriteria.trim().isEmpty()) {
            return Collections.emptyList();
        }

        Query multiMatchQuery = Query.of(q -> q.multiMatch(m -> m
                .fields("psychologicalProfile", "tacticalNotes", "weaknesses", "tags^2")
                .query(textCriteria)
                .fuzziness("AUTO")
                .analyzer("english")
        ));

        FetchSourceFilter sourceFilter = new FetchSourceFilter(new String[]{"playerId"}, null);

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(multiMatchQuery)
                .withSourceFilter(sourceFilter)
                .build();

        SearchHits<Report> searchHits = elasticsearchTemplate.search(nativeQuery, Report.class);

        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(Report::getPlayerId)
                .distinct()
                .collect(Collectors.toList());
    }
}
