package rs.ac.uns.acs.nais.TicketSearchService.service.impl;

import org.springframework.stereotype.Service;
import rs.ac.uns.acs.nais.TicketSearchService.model.elastic.EventDocument;
import rs.ac.uns.acs.nais.TicketSearchService.repository.elastic.EventElasticRepository;
import rs.ac.uns.acs.nais.TicketSearchService.repository.elastic.EventElasticRepositoryCustom;
import rs.ac.uns.acs.nais.TicketSearchService.service.IEventElasticService;

import java.util.*;

@Service
public class EventElasticService implements IEventElasticService {

    private final EventElasticRepository repository;
    private final EventElasticRepositoryCustom customRepository;

    public EventElasticService(EventElasticRepository repository, EventElasticRepositoryCustom customRepository) {
        this.repository = repository;
        this.customRepository = customRepository;
    }

    @Override public EventDocument save(EventDocument document) { return repository.save(document); }
    @Override public Optional<EventDocument> findById(String id) { return repository.findById(id); }
    @Override public List<EventDocument> findAll() { List<EventDocument> r = new ArrayList<>(); repository.findAll().forEach(r::add); return r; }
    @Override public List<EventDocument> findByEventType(String eventType) { return repository.findByEventType(eventType); }
    @Override public List<EventDocument> findByStatus(String status) { return repository.findByStatus(status); }
    @Override public List<EventDocument> findByCity(String city) { return repository.findByCity(city); }
    @Override public List<EventDocument> findByPriceBetween(Double min, Double max) { return repository.findByBasePriceBetween(min, max); }
    @Override public void deleteById(String id) { repository.deleteById(id); }
    @Override public Map<String, Object> searchEventsFullText(String searchText, String eventType, String status, Double minPrice, Double maxPrice, String sortBy, String sortDir, int page, int size) {
        return customRepository.searchEventsFullText(searchText, eventType, status, minPrice, maxPrice, sortBy, sortDir, page, size);
    }
}
