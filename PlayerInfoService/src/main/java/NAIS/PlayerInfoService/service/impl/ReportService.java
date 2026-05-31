package NAIS.PlayerInfoService.service.impl;

import NAIS.PlayerInfoService.dto.PlayerTierResponse;
import NAIS.PlayerInfoService.dto.ReportDTO;
import NAIS.PlayerInfoService.dto.TacticalTrendResponse;
import NAIS.PlayerInfoService.model.Report;
import NAIS.PlayerInfoService.repository.ReportRepository;
import NAIS.PlayerInfoService.service.IReportService;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    public TacticalTrendResponse analyzeTacticalTrends(String tacticalSearch, String requiredTag) {
        Query tacticalFullText = Query.of(q -> q.match(m -> m.field("tacticalNotes").query(tacticalSearch)));
        Query tagFilter = Query.of(q -> q.term(t -> t.field("tags").value(requiredTag)));

        Query timeFilter = Query.of(q -> q.range(r -> r.date(d -> d.field("createdAt").gte("now-1y"))));

        Query boolQuery = Query.of(b -> b.bool(bool -> bool
                .must(tacticalFullText)
                .filter(tagFilter, timeFilter)
        ));

        Aggregation topHitsSubAgg = Aggregation.of(a -> a.topHits(th -> th
                .size(1)
                .sort(s -> s.field(f -> f.field("overallRating").order(SortOrder.Desc)))
                .source(sc -> sc.filter(f -> f.includes(List.of("playerId", "overallRating"))))
        ));

        Aggregation scoutsAgg = Aggregation.of(a -> a.terms(t -> t.field("scoutId"))
                .aggregations("best-report", topHitsSubAgg));

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(boolQuery)
                .withSort(s -> s.field(f -> f.field("createdAt").order(SortOrder.Desc)))
                .withMaxResults(150)
                .withAggregation("scout-analysis", scoutsAgg)
                .build();

        return convertSearchHitsToTacticalTrendResponse(elasticsearchTemplate.search(nativeQuery, Report.class));
    }

    public PlayerTierResponse analyzePlayerTiers(String searchTerms) {
        Query fuzzySearch = Query.of(q -> q.match(m -> m
                .field("tacticalNotes")
                .query(searchTerms)
                .fuzziness("AUTO")
        ));

        Aggregation topTagsSubAgg = Aggregation.of(a -> a.terms(t -> t.field("tags").size(3)));

        Aggregation tierRangeAgg = Aggregation.of(a -> a.range(r -> r
                .field("overallRating")
                .ranges(List.of(
                        AggregationRange.of(ar -> ar.to(6.0).key("Risk (Below 6)")),
                        AggregationRange.of(ar -> ar.from(6.0).to(8.0).key("Rotation (6-7)")),
                        AggregationRange.of(ar -> ar.from(8.0).key("Elite (8 or higher)"))
                ))
        ).aggregations("frequent_tags", topTagsSubAgg));

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(fuzzySearch)
                .withSort(s -> s.field(f -> f.field("_score").order(SortOrder.Desc)))
                .withAggregation("player-categories", tierRangeAgg)
                .build();

        return convertSeachHitsToPlayerTierResponse(elasticsearchTemplate.search(nativeQuery, Report.class));
    }

    private TacticalTrendResponse convertSearchHitsToTacticalTrendResponse(SearchHits<Report> hits) {
        List<Report> prospects = hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        List<TacticalTrendResponse.ScoutMaxRating> scoutStats = new ArrayList<>();

        if (hits.hasAggregations()) {
            ElasticsearchAggregations esAggs = (ElasticsearchAggregations) hits.getAggregations();
            Map<String, ElasticsearchAggregation> aggMap = esAggs.aggregationsAsMap();

            Aggregate skautiAnaliza = aggMap.get("scout-analysis").aggregation().getAggregate();

            if (skautiAnaliza != null && skautiAnaliza.isSterms()) {
                for (StringTermsBucket bucket : skautiAnaliza.sterms().buckets().array()) {
                    String scoutId = bucket.key().stringValue();
                    Aggregate najboljiIzvestajAgg = bucket.aggregations().get("best-report");

                    if (najboljiIzvestajAgg != null && najboljiIzvestajAgg.isTopHits()) {
                        var hitsList = najboljiIzvestajAgg.topHits().hits().hits();
                        if (!hitsList.isEmpty()) {
                            var topHit = hitsList.get(0);
                            Map<String, Object> sourceMap = topHit.source().to(Map.class);

                            String topPlayerId = (String) sourceMap.get("playerId");
                            double maxRating = Double.parseDouble(sourceMap.get("overallRating").toString());

                            scoutStats.add(new TacticalTrendResponse.ScoutMaxRating(scoutId, maxRating, topPlayerId));
                        }
                    }
                }
            }
        }

        return new TacticalTrendResponse(prospects, scoutStats);
    }

    public PlayerTierResponse convertSeachHitsToPlayerTierResponse(SearchHits<Report> hits) {
        List<Report> reports = hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        List<PlayerTierResponse.TierStat> stats = new ArrayList<>();

        if (hits.hasAggregations()) {
            ElasticsearchAggregations esAggs = (ElasticsearchAggregations) hits.getAggregations();
            Map<String, ElasticsearchAggregation> aggMap = esAggs.aggregationsAsMap();

            Aggregate kategorijeAgg = aggMap.get("player-categories").aggregation().getAggregate();

            if (kategorijeAgg != null && kategorijeAgg.isRange()) {
                for (RangeBucket bucket : kategorijeAgg.range().buckets().array()) {
                    String tierName = bucket.key();
                    long count = bucket.docCount();
                    List<String> topTags = new ArrayList<>();

                    Aggregate tagoviAgg = bucket.aggregations().get("frequent_tags");
                    if (tagoviAgg != null && tagoviAgg.isSterms()) {
                        for (StringTermsBucket tagBucket : tagoviAgg.sterms().buckets().array()) {
                            topTags.add(tagBucket.key().stringValue());
                        }
                    }

                    stats.add(new PlayerTierResponse.TierStat(tierName, count, topTags));
                }
            }
        }

        return new PlayerTierResponse(reports, stats);
    }
}
