package rs.ac.uns.acs.nais.TicketSearchService.repository.elastic;

import java.util.Map;

public interface EventElasticRepositoryCustom {
    Map<String, Object> searchEventsFullText(String searchText, String eventType, String status,
                                             Double minPrice, Double maxPrice,
                                             String sortBy, String sortDir,
                                             int page, int size);
}
