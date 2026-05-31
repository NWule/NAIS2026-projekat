package rs.ac.uns.acs.nais.TicketSearchService.repository.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.acs.nais.TicketSearchService.model.elastic.TicketSaleDocument;

import java.util.List;

@Repository
public interface TicketSaleElasticRepository extends ElasticsearchRepository<TicketSaleDocument, String> {
    List<TicketSaleDocument> findByCustomerId(String customerId);
    List<TicketSaleDocument> findByEventId(String eventId);
    List<TicketSaleDocument> findByEventType(String eventType);
    List<TicketSaleDocument> findByTicketType(String ticketType);
    List<TicketSaleDocument> findByPaymentMethod(String paymentMethod);
    List<TicketSaleDocument> findByZoneName(String zoneName);
    List<TicketSaleDocument> findByPriceBetween(Double minPrice, Double maxPrice);
}
