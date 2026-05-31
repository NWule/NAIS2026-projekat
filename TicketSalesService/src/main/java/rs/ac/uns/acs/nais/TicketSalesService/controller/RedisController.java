package rs.ac.uns.acs.nais.TicketSalesService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.acs.nais.TicketSalesService.service.IRedisService;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/redis")
public class RedisController {

    private final IRedisService redisService;

    public RedisController(IRedisService redisService) {
        this.redisService = redisService;
    }

    @PostMapping("/seat/reserve")
    public ResponseEntity<Map<String, Object>> reserveSeat(
            @RequestParam String eventId,
            @RequestParam String seatId,
            @RequestParam String customerId) {
        boolean success = redisService.reserveSeat(eventId, seatId, customerId);
        if (success) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Sediste rezervisano na 10 minuta",
                    "eventId", eventId,
                    "seatId", seatId,
                    "customerId", customerId
            ));
        }
        return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Sediste je vec rezervisano"
        ));
    }

    @GetMapping("/seat/status")
    public ResponseEntity<Map<String, Object>> getSeatStatus(
            @RequestParam String eventId,
            @RequestParam String seatId) {
        Optional<String> reservation = redisService.getSeatReservation(eventId, seatId);
        if (reservation.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "reserved", true,
                    "reservedBy", reservation.get(),
                    "eventId", eventId,
                    "seatId", seatId
            ));
        }
        return ResponseEntity.ok(Map.of("reserved", false, "eventId", eventId, "seatId", seatId));
    }

    @DeleteMapping("/seat/release")
    public ResponseEntity<Map<String, Object>> releaseSeat(
            @RequestParam String eventId,
            @RequestParam String seatId) {
        boolean released = redisService.releaseSeatReservation(eventId, seatId);
        return ResponseEntity.ok(Map.of("released", released, "eventId", eventId, "seatId", seatId));
    }

    @PostMapping("/price/cache")
    public ResponseEntity<Void> cachePrice(
            @RequestParam String eventId,
            @RequestParam String zoneId,
            @RequestParam Double price) {
        redisService.cacheTicketPrice(eventId, zoneId, price);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/price")
    public ResponseEntity<Map<String, Object>> getPrice(
            @RequestParam String eventId,
            @RequestParam String zoneId) {
        Optional<Double> price = redisService.getCachedTicketPrice(eventId, zoneId);
        if (price.isPresent()) {
            return ResponseEntity.ok(Map.of("cached", true, "price", price.get(), "eventId", eventId, "zoneId", zoneId));
        }
        return ResponseEntity.ok(Map.of("cached", false, "eventId", eventId, "zoneId", zoneId));
    }

    @DeleteMapping("/price/invalidate/{eventId}")
    public ResponseEntity<Void> invalidatePrices(@PathVariable String eventId) {
        redisService.invalidateEventPrices(eventId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/event/cache")
    public ResponseEntity<Void> cacheEvent(
            @RequestParam String eventId,
            @RequestBody String eventJson) {
        redisService.cacheEventData(eventId, eventJson);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<Map<String, Object>> getCachedEvent(@PathVariable String eventId) {
        Optional<String> data = redisService.getCachedEventData(eventId);
        if (data.isPresent()) {
            return ResponseEntity.ok(Map.of("cached", true, "data", data.get()));
        }
        return ResponseEntity.ok(Map.of("cached", false));
    }

    @DeleteMapping("/event/{eventId}")
    public ResponseEntity<Void> invalidateEvent(@PathVariable String eventId) {
        redisService.invalidateEventData(eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public Map<String, Object> getCacheStats() {
        return redisService.getCacheStats();
    }
}
