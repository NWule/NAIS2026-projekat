package rs.ac.uns.acs.nais.TicketSalesService.repository;

import java.util.List;
import java.util.Map;

public interface EventRepositoryCustom {
    List<Map<String, Object>> getRevenueByEventAndZone();
    List<Map<String, Object>> getZoneOccupancyForUpcomingEvents();
    List<Map<String, Object>> updateDynamicPricing(String eventId);
}
