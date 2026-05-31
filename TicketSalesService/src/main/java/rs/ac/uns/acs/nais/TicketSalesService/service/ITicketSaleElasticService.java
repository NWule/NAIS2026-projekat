package rs.ac.uns.acs.nais.TicketSalesService.service;

import rs.ac.uns.acs.nais.TicketSalesService.model.elastic.TicketSaleDocument;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ITicketSaleElasticService {

    TicketSaleDocument save(TicketSaleDocument document);

    Optional<TicketSaleDocument> findById(String id);

    List<TicketSaleDocument> findAll();

    List<TicketSaleDocument> findByCustomerId(String customerId);

    List<TicketSaleDocument> findByEventId(String eventId);

    List<TicketSaleDocument> findByEventType(String eventType);

    List<TicketSaleDocument> findByPaymentMethod(String paymentMethod);

    List<TicketSaleDocument> findByPriceBetween(Double minPrice, Double maxPrice);

    void deleteById(String id);

    // Složeni upiti
    List<Map<String, Object>> getSalesAnalyticsByEventTypeAndPaymentMethod(String startDate, String endDate);

    List<Map<String, Object>> getTopEventsByRevenue(int limit, String eventType);
}
