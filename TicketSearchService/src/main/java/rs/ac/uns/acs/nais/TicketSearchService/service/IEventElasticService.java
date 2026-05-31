package rs.ac.uns.acs.nais.TicketSearchService.service;

import rs.ac.uns.acs.nais.TicketSearchService.model.elastic.EventDocument;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IEventElasticService {
    EventDocument save(EventDocument document);
    Optional<EventDocument> findById(String id);
    List<EventDocument> findAll();
    List<EventDocument> findByEventType(String eventType);
    List<EventDocument> findByStatus(String status);
    List<EventDocument> findByCity(String city);
    List<EventDocument> findByPriceBetween(Double minPrice, Double maxPrice);
    void deleteById(String id);
    Map<String, Object> searchEventsFullText(String searchText, String eventType, String status,
                                             Double minPrice, Double maxPrice,
                                             String sortBy, String sortDir,
                                             int page, int size);
}
