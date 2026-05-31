package rs.ac.uns.acs.nais.TicketSalesService.repository.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.NamedValue;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TicketSaleElasticRepositoryCustomImpl implements TicketSaleElasticRepositoryCustom {

    private final ElasticsearchClient elasticsearchClient;

    public TicketSaleElasticRepositoryCustomImpl(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public List<Map<String, Object>> getSalesAnalyticsByEventTypeAndPaymentMethod(String startDate, String endDate) {
        List<Map<String, Object>> results = new ArrayList<>();
        try {
            SearchResponse<Void> response = elasticsearchClient.search(s -> s
                .index("ticket_sales")
                .size(0)
                .query(q -> q
                    .range(r -> r
                        .field("purchaseDate")
                        .gte(JsonData.of(startDate))
                        .lte(JsonData.of(endDate))
                    )
                )
                .aggregations("by_event_type", a -> a
                    .terms(t -> t
                        .field("eventType")
                        .size(20)
                        .order(List.of(NamedValue.of("event_type_revenue", SortOrder.Desc)))
                    )
                    .aggregations("event_type_revenue", ra -> ra
                        .sum(su -> su.field("price"))
                    )
                    .aggregations("by_payment_method", pa -> pa
                        .terms(pt -> pt.field("paymentMethod").size(10))
                        .aggregations("payment_revenue", pr -> pr
                            .sum(su -> su.field("price"))
                        )
                        .aggregations("ticket_count", tc -> tc
                            .valueCount(vc -> vc.field("customerId"))
                        )
                        .aggregations("avg_discount", ad -> ad
                            .avg(av -> av.field("discountPercent"))
                        )
                    )
                ),
                Void.class
            );

            var byEventType = response.aggregations().get("by_event_type").sterms();
            for (StringTermsBucket eventBucket : byEventType.buckets().array()) {
                double eventRevenue = eventBucket.aggregations().get("event_type_revenue").sum().value();
                var byPayment = eventBucket.aggregations().get("by_payment_method").sterms();

                for (StringTermsBucket paymentBucket : byPayment.buckets().array()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("eventType", eventBucket.key().stringValue());
                    row.put("paymentMethod", paymentBucket.key().stringValue());
                    row.put("totalEventRevenue", eventRevenue);
                    row.put("paymentRevenue", paymentBucket.aggregations().get("payment_revenue").sum().value());
                    row.put("ticketCount", paymentBucket.aggregations().get("ticket_count").valueCount().value());
                    row.put("avgDiscount", paymentBucket.aggregations().get("avg_discount").avg().value());
                    results.add(row);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Greška pri dohvatanju analytics podataka", e);
        }
        return results;
    }

    @Override
    public List<Map<String, Object>> getTopEventsByRevenue(int limit, String eventType) {
        List<Map<String, Object>> results = new ArrayList<>();
        try {
            SearchResponse<Void> response = elasticsearchClient.search(s -> {
                var builder = s
                    .index("ticket_sales")
                    .size(0);

                if (eventType != null && !eventType.isBlank()) {
                    builder.query(q -> q
                        .term(t -> t.field("eventType").value(eventType))
                    );
                }

                return builder.aggregations("top_events", a -> a
                    .terms(t -> t
                        .field("eventName.keyword")
                        .size(limit)
                        .order(List.of(NamedValue.of("total_revenue", SortOrder.Desc)))
                    )
                    .aggregations("total_revenue", ra -> ra
                        .sum(su -> su.field("price"))
                    )
                    .aggregations("ticket_count", tc -> tc
                        .valueCount(vc -> vc.field("customerId"))
                    )
                    .aggregations("avg_price", ap -> ap
                        .avg(av -> av.field("price"))
                    )
                    .aggregations("avg_discount", ad -> ad
                        .avg(av -> av.field("discountPercent"))
                    )
                );
            }, Void.class);

            var topEvents = response.aggregations().get("top_events").sterms();
            for (StringTermsBucket bucket : topEvents.buckets().array()) {
                Map<String, Object> row = new HashMap<>();
                row.put("eventName", bucket.key().stringValue());
                row.put("totalRevenue", bucket.aggregations().get("total_revenue").sum().value());
                row.put("ticketCount", bucket.aggregations().get("ticket_count").valueCount().value());
                row.put("avgPrice", bucket.aggregations().get("avg_price").avg().value());
                row.put("avgDiscount", bucket.aggregations().get("avg_discount").avg().value());
                results.add(row);
            }
        } catch (IOException e) {
            throw new RuntimeException("Greška pri dohvatanju top dogadjaja", e);
        }
        return results;
    }
}
