package rs.ac.uns.acs.nais.TicketSearchService.service.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import rs.ac.uns.acs.nais.TicketSearchService.service.IRedisService;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService implements IRedisService {

    private static final long SEAT_RESERVATION_TTL = 600;
    private static final long TICKET_PRICE_TTL = 300;
    private static final long EVENT_DATA_TTL = 600;

    private final RedisTemplate<String, String> redisTemplate;

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String seatKey(String eventId, String seatId) { return "seat:reservation:" + eventId + ":" + seatId; }
    private String priceKey(String eventId, String zoneId) { return "ticket:price:" + eventId + ":" + zoneId; }
    private String eventKey(String eventId) { return "event:cache:" + eventId; }

    @Override
    public boolean reserveSeat(String eventId, String seatId, String customerId) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(seatKey(eventId, seatId), customerId, SEAT_RESERVATION_TTL, TimeUnit.SECONDS));
    }

    @Override
    public Optional<String> getSeatReservation(String eventId, String seatId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(seatKey(eventId, seatId)));
    }

    @Override
    public boolean releaseSeatReservation(String eventId, String seatId) {
        return Boolean.TRUE.equals(redisTemplate.delete(seatKey(eventId, seatId)));
    }

    @Override
    public void cacheTicketPrice(String eventId, String zoneId, Double price) {
        redisTemplate.opsForValue().set(priceKey(eventId, zoneId), String.valueOf(price), TICKET_PRICE_TTL, TimeUnit.SECONDS);
    }

    @Override
    public Optional<Double> getCachedTicketPrice(String eventId, String zoneId) {
        String value = redisTemplate.opsForValue().get(priceKey(eventId, zoneId));
        return value == null ? Optional.empty() : Optional.of(Double.parseDouble(value));
    }

    @Override
    public void invalidateEventPrices(String eventId) {
        Set<String> keys = redisTemplate.keys("ticket:price:" + eventId + ":*");
        if (keys != null && !keys.isEmpty()) redisTemplate.delete(keys);
    }

    @Override
    public void cacheEventData(String eventId, String eventJson) {
        redisTemplate.opsForValue().set(eventKey(eventId), eventJson, EVENT_DATA_TTL, TimeUnit.SECONDS);
    }

    @Override
    public Optional<String> getCachedEventData(String eventId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(eventKey(eventId)));
    }

    @Override
    public void invalidateEventData(String eventId) { redisTemplate.delete(eventKey(eventId)); }

    @Override
    public Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        Set<String> reservationKeys = redisTemplate.keys("seat:reservation:*");
        Set<String> priceKeys = redisTemplate.keys("ticket:price:*");
        Set<String> eventKeys = redisTemplate.keys("event:cache:*");
        stats.put("activeSeatReservations", reservationKeys != null ? reservationKeys.size() : 0);
        stats.put("cachedTicketPrices", priceKeys != null ? priceKeys.size() : 0);
        stats.put("cachedEvents", eventKeys != null ? eventKeys.size() : 0);
        if (reservationKeys != null) stats.put("reservationKeys", reservationKeys);
        return stats;
    }
}
