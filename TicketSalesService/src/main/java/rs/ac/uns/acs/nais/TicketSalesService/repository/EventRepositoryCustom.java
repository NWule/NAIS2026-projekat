package rs.ac.uns.acs.nais.TicketSalesService.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EventRepositoryCustom {
    List<Map<String, Object>> getRevenueByEventAndZone();
    List<Map<String, Object>> getZoneOccupancyForUpcomingEvents();
    List<Map<String, Object>> updateDynamicPricing(String eventId);
    Optional<Map<String, Object>> findEventDataBySeatId(String seatId);
}
