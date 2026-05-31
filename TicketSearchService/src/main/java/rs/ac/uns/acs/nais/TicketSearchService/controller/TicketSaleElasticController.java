package rs.ac.uns.acs.nais.TicketSearchService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.acs.nais.TicketSearchService.model.elastic.TicketSaleDocument;
import rs.ac.uns.acs.nais.TicketSearchService.service.ITicketSaleElasticService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ticket-sales")
public class TicketSaleElasticController {

    private final ITicketSaleElasticService service;

    public TicketSaleElasticController(ITicketSaleElasticService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TicketSaleDocument> create(@RequestBody TicketSaleDocument document) { return ResponseEntity.ok(service.save(document)); }

    @GetMapping
    public List<TicketSaleDocument> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<TicketSaleDocument> findById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketSaleDocument> update(@PathVariable String id, @RequestBody TicketSaleDocument document) {
        return service.findById(id).map(e -> { document.setId(id); return ResponseEntity.ok(service.save(document)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) { service.deleteById(id); return ResponseEntity.noContent().build(); }

    @GetMapping("/customer/{customerId}")
    public List<TicketSaleDocument> findByCustomer(@PathVariable String customerId) { return service.findByCustomerId(customerId); }

    @GetMapping("/event/{eventId}")
    public List<TicketSaleDocument> findByEvent(@PathVariable String eventId) { return service.findByEventId(eventId); }

    @GetMapping("/event-type/{eventType}")
    public List<TicketSaleDocument> findByEventType(@PathVariable String eventType) { return service.findByEventType(eventType); }

    @GetMapping("/payment-method/{paymentMethod}")
    public List<TicketSaleDocument> findByPaymentMethod(@PathVariable String paymentMethod) { return service.findByPaymentMethod(paymentMethod); }

    @GetMapping("/price-range")
    public List<TicketSaleDocument> findByPriceRange(@RequestParam Double minPrice, @RequestParam Double maxPrice) { return service.findByPriceBetween(minPrice, maxPrice); }

    @GetMapping("/analytics/by-event-type-payment")
    public List<Map<String, Object>> getSalesAnalytics(@RequestParam(defaultValue = "2020-01-01") String startDate, @RequestParam(defaultValue = "2030-12-31") String endDate) {
        return service.getSalesAnalyticsByEventTypeAndPaymentMethod(startDate, endDate);
    }

    @GetMapping("/analytics/top-events")
    public List<Map<String, Object>> getTopEvents(@RequestParam(defaultValue = "10") int limit, @RequestParam(required = false) String eventType) {
        return service.getTopEventsByRevenue(limit, eventType);
    }
}
