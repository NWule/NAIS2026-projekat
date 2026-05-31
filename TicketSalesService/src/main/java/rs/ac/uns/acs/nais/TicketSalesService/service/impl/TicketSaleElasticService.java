package rs.ac.uns.acs.nais.TicketSalesService.service.impl;

import org.springframework.stereotype.Service;
import rs.ac.uns.acs.nais.TicketSalesService.model.elastic.TicketSaleDocument;
import rs.ac.uns.acs.nais.TicketSalesService.repository.elastic.TicketSaleElasticRepository;
import rs.ac.uns.acs.nais.TicketSalesService.repository.elastic.TicketSaleElasticRepositoryCustom;
import rs.ac.uns.acs.nais.TicketSalesService.service.ITicketSaleElasticService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TicketSaleElasticService implements ITicketSaleElasticService {

    private final TicketSaleElasticRepository repository;
    private final TicketSaleElasticRepositoryCustom customRepository;

    public TicketSaleElasticService(TicketSaleElasticRepository repository,
                                    TicketSaleElasticRepositoryCustom customRepository) {
        this.repository = repository;
        this.customRepository = customRepository;
    }

    @Override
    public TicketSaleDocument save(TicketSaleDocument document) {
        return repository.save(document);
    }

    @Override
    public Optional<TicketSaleDocument> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<TicketSaleDocument> findAll() {
        List<TicketSaleDocument> result = new ArrayList<>();
        repository.findAll().forEach(result::add);
        return result;
    }

    @Override
    public List<TicketSaleDocument> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    public List<TicketSaleDocument> findByEventId(String eventId) {
        return repository.findByEventId(eventId);
    }

    @Override
    public List<TicketSaleDocument> findByEventType(String eventType) {
        return repository.findByEventType(eventType);
    }

    @Override
    public List<TicketSaleDocument> findByPaymentMethod(String paymentMethod) {
        return repository.findByPaymentMethod(paymentMethod);
    }

    @Override
    public List<TicketSaleDocument> findByPriceBetween(Double minPrice, Double maxPrice) {
        return repository.findByPriceBetween(minPrice, maxPrice);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public List<Map<String, Object>> getSalesAnalyticsByEventTypeAndPaymentMethod(String startDate, String endDate) {
        return customRepository.getSalesAnalyticsByEventTypeAndPaymentMethod(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getTopEventsByRevenue(int limit, String eventType) {
        return customRepository.getTopEventsByRevenue(limit, eventType);
    }
}
