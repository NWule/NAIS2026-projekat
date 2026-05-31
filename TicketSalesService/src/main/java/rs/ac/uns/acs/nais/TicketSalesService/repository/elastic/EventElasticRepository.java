package rs.ac.uns.acs.nais.TicketSalesService.repository.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.acs.nais.TicketSalesService.model.elastic.EventDocument;

import java.util.List;

@Repository
public interface EventElasticRepository extends ElasticsearchRepository<EventDocument, String> {

    List<EventDocument> findByEventType(String eventType);

    List<EventDocument> findByStatus(String status);

    List<EventDocument> findByCity(String city);

    List<EventDocument> findByBasePriceBetween(Double minPrice, Double maxPrice);

    List<EventDocument> findByEventTypeAndStatus(String eventType, String status);
}
