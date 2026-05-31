package rs.ac.uns.acs.nais.TicketSalesService.service;

import java.util.Map;
import java.util.Optional;

public interface IRedisService {

    // Privremena rezervacija sedišta (TTL: 10 minuta)
    boolean reserveSeat(String eventId, String seatId, String customerId);

    Optional<String> getSeatReservation(String eventId, String seatId);

    boolean releaseSeatReservation(String eventId, String seatId);

    // Keš cene karte po dogadjaju i zoni (TTL: 5 minuta)
    void cacheTicketPrice(String eventId, String zoneId, Double price);

    Optional<Double> getCachedTicketPrice(String eventId, String zoneId);

    void invalidateEventPrices(String eventId);

    // Keš detalja dogadjaja (TTL: 10 minuta)
    void cacheEventData(String eventId, String eventJson);

    Optional<String> getCachedEventData(String eventId);

    void invalidateEventData(String eventId);

    // Statistike keša
    Map<String, Object> getCacheStats();
}
