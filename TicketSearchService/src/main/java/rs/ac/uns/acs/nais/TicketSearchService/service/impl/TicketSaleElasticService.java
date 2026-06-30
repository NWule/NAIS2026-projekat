package rs.ac.uns.acs.nais.TicketSearchService.service.impl;

import org.springframework.stereotype.Service;
import rs.ac.uns.acs.nais.TicketSearchService.model.elastic.TicketSaleDocument;
import rs.ac.uns.acs.nais.TicketSearchService.repository.elastic.TicketSaleElasticRepository;
import rs.ac.uns.acs.nais.TicketSearchService.repository.elastic.TicketSaleElasticRepositoryCustom;
import rs.ac.uns.acs.nais.TicketSearchService.service.ITicketSaleElasticService;

import java.util.*;

@Service
public class TicketSaleElasticService implements ITicketSaleElasticService {

    private final TicketSaleElasticRepository repository;
    private final TicketSaleElasticRepositoryCustom customRepository;

    public TicketSaleElasticService(TicketSaleElasticRepository repository,
                                    TicketSaleElasticRepositoryCustom customRepository) {
        this.repository = repository;
        this.customRepository = customRepository;
    }

    @Override public TicketSaleDocument save(TicketSaleDocument document) { return repository.save(document); }
    @Override public Optional<TicketSaleDocument> findById(String id) { return repository.findById(id); }
    @Override public List<TicketSaleDocument> findAll() { List<TicketSaleDocument> r = new ArrayList<>(); repository.findAll().forEach(r::add); return r; }
    @Override public List<TicketSaleDocument> findByCustomerId(String customerId) { return repository.findByCustomerId(customerId); }
    @Override public List<TicketSaleDocument> findByEventId(String eventId) { return repository.findByEventId(eventId); }
    @Override public List<TicketSaleDocument> findByEventType(String eventType) { return repository.findByEventType(eventType); }
    @Override public List<TicketSaleDocument> findByPaymentMethod(String paymentMethod) { return repository.findByPaymentMethod(paymentMethod); }
    @Override public List<TicketSaleDocument> findByPriceBetween(Double min, Double max) { return repository.findByPriceBetween(min, max); }
    @Override public void deleteById(String id) { repository.deleteById(id); }
    @Override public List<Map<String, Object>> getSalesAnalyticsByEventTypeAndPaymentMethod(String s, String e) { return customRepository.getSalesAnalyticsByEventTypeAndPaymentMethod(s, e); }
    @Override public List<Map<String, Object>> getTopEventsByRevenue(int limit, String eventType) { return customRepository.getTopEventsByRevenue(limit, eventType); }
    @Override public List<Map<String, Object>> getRevenueByZone() { return customRepository.getRevenueByZone(); }
}
