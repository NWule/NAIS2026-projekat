package rs.ac.uns.acs.nais.TicketSearchService.service;

import java.util.Map;
import java.util.Optional;

public interface IRedisService {
    boolean reserveSeat(String eventId, String seatId, String customerId);
    Optional<String> getSeatReservation(String eventId, String seatId);
    boolean releaseSeatReservation(String eventId, String seatId);
    void cacheTicketPrice(String eventId, String zoneId, Double price);
    Optional<Double> getCachedTicketPrice(String eventId, String zoneId);
    void invalidateEventPrices(String eventId);
    void cacheEventData(String eventId, String eventJson);
    Optional<String> getCachedEventData(String eventId);
    void invalidateEventData(String eventId);
    Map<String, Object> getCacheStats();
}
