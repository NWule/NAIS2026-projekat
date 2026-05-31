package rs.ac.uns.acs.nais.TicketSalesService.repository.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import org.springframework.stereotype.Repository;
import rs.ac.uns.acs.nais.TicketSalesService.model.elastic.EventDocument;

import java.io.IOException;
import java.util.*;

@Repository
public class EventElasticRepositoryCustomImpl implements EventElasticRepositoryCustom {

    private final ElasticsearchClient elasticsearchClient;

    public EventElasticRepositoryCustomImpl(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public Map<String, Object> searchEventsFullText(String searchText, String eventType, String status,
                                                    Double minPrice, Double maxPrice,
                                                    String sortBy, String sortDir,
                                                    int page, int size) {
        try {
            SortOrder order = "asc".equalsIgnoreCase(sortDir) ? SortOrder.Asc : SortOrder.Desc;
            String sortField = (sortBy != null && !sortBy.isBlank()) ? sortBy : "date";

            SearchResponse<EventDocument> response = elasticsearchClient.search(s -> {
                var builder = s
                    .index("events")
                    .from(page * size)
                    .size(size)
                    .sort(so -> so.field(f -> f.field(sortField).order(order)));

                builder.query(q -> q.bool(b -> {
                    if (searchText != null && !searchText.isBlank()) {
                        b.must(m -> m.multiMatch(mm -> mm
                            .query(searchText)
                            .fields("name^3", "venue^2", "description", "organizer")
                        ));
                    } else {
                        b.must(m -> m.matchAll(ma -> ma));
                    }
                    if (eventType != null && !eventType.isBlank()) {
                        b.filter(f -> f.term(t -> t.field("eventType").value(eventType)));
                    }
                    if (status != null && !status.isBlank()) {
                        b.filter(f -> f.term(t -> t.field("status").value(status)));
                    }
                    if (minPrice != null) {
                        b.filter(f -> f.range(r -> r.field("basePrice").gte(JsonData.of(minPrice))));
                    }
                    if (maxPrice != null) {
                        b.filter(f -> f.range(r -> r.field("basePrice").lte(JsonData.of(maxPrice))));
                    }
                    return b;
                }));

                builder.aggregations("by_competition", a -> a
                    .terms(t -> t.field("eventType").size(10))
                    .aggregations("avg_price", ap -> ap.avg(av -> av.field("basePrice")))
                    .aggregations("total_capacity", tc -> tc.sum(su -> su.field("totalCapacity")))
                );

                builder.aggregations("by_status", a -> a
                    .terms(t -> t.field("status").size(5))
                    .aggregations("available_seats_sum", as -> as.sum(su -> su.field("availableSeats")))
                );

                return builder;
            }, EventDocument.class);

            List<EventDocument> hits = new ArrayList<>();
            response.hits().hits().forEach(hit -> hits.add(hit.source()));

            List<Map<String, Object>> byCompetition = new ArrayList<>();
            var compAgg = response.aggregations().get("by_competition").sterms();
            for (StringTermsBucket bucket : compAgg.buckets().array()) {
                Map<String, Object> row = new HashMap<>();
                row.put("competition", bucket.key().stringValue());
                row.put("matchCount", bucket.docCount());
                row.put("avgPrice", bucket.aggregations().get("avg_price").avg().value());
                row.put("totalCapacity", bucket.aggregations().get("total_capacity").sum().value());
                byCompetition.add(row);
            }

            List<Map<String, Object>> byStatus = new ArrayList<>();
            var statusAgg = response.aggregations().get("by_status").sterms();
            for (StringTermsBucket bucket : statusAgg.buckets().array()) {
                Map<String, Object> row = new HashMap<>();
                row.put("status", bucket.key().stringValue());
                row.put("count", bucket.docCount());
                row.put("availableSeatsSum", bucket.aggregations().get("available_seats_sum").sum().value());
                byStatus.add(row);
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("totalHits", response.hits().total() != null ? response.hits().total().value() : 0);
            result.put("page", page);
            result.put("size", size);
            result.put("results", hits);
            result.put("byCompetition", byCompetition);
            result.put("byStatus", byStatus);
            return result;

        } catch (IOException e) {
            throw new RuntimeException("Greška pri pretrazi dogadjaja", e);
        }
    }
}
