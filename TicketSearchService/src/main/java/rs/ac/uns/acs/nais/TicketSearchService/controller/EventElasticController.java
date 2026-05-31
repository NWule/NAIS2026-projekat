package rs.ac.uns.acs.nais.TicketSearchService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.acs.nais.TicketSearchService.model.elastic.EventDocument;
import rs.ac.uns.acs.nais.TicketSearchService.service.IEventElasticService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/events")
public class EventElasticController {

    private final IEventElasticService service;

    public EventElasticController(IEventElasticService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EventDocument> create(@RequestBody EventDocument document) { return ResponseEntity.ok(service.save(document)); }

    @GetMapping
    public List<EventDocument> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<EventDocument> findById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDocument> update(@PathVariable String id, @RequestBody EventDocument document) {
        return service.findById(id).map(e -> { document.setId(id); return ResponseEntity.ok(service.save(document)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) { service.deleteById(id); return ResponseEntity.noContent().build(); }

    @GetMapping("/event-type/{eventType}")
    public List<EventDocument> findByEventType(@PathVariable String eventType) { return service.findByEventType(eventType); }

    @GetMapping("/status/{status}")
    public List<EventDocument> findByStatus(@PathVariable String status) { return service.findByStatus(status); }

    @GetMapping("/city/{city}")
    public List<EventDocument> findByCity(@PathVariable String city) { return service.findByCity(city); }

    @GetMapping("/price-range")
    public List<EventDocument> findByPriceRange(@RequestParam Double minPrice, @RequestParam Double maxPrice) { return service.findByPriceBetween(minPrice, maxPrice); }

    @GetMapping("/search")
    public Map<String, Object> search(@RequestParam(required = false) String q,
                                      @RequestParam(required = false) String eventType,
                                      @RequestParam(required = false) String status,
                                      @RequestParam(required = false) Double minPrice,
                                      @RequestParam(required = false) Double maxPrice,
                                      @RequestParam(defaultValue = "date") String sortBy,
                                      @RequestParam(defaultValue = "asc") String sortDir,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size) {
        return service.searchEventsFullText(q, eventType, status, minPrice, maxPrice, sortBy, sortDir, page, size);
    }
}
