package rs.ac.uns.acs.nais.TicketSearchService.repository.elastic;

import java.util.List;
import java.util.Map;

public interface TicketSaleElasticRepositoryCustom {
    List<Map<String, Object>> getSalesAnalyticsByEventTypeAndPaymentMethod(String startDate, String endDate);
    List<Map<String, Object>> getTopEventsByRevenue(int limit, String eventType);
    List<Map<String, Object>> getRevenueByZone();
}
