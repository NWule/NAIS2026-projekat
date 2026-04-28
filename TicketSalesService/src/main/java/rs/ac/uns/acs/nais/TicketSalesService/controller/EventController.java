package rs.ac.uns.acs.nais.TicketSalesService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.acs.nais.TicketSalesService.dto.EventDTO;
import rs.ac.uns.acs.nais.TicketSalesService.dto.EventZoneDTO;
import rs.ac.uns.acs.nais.TicketSalesService.model.Event;
import rs.ac.uns.acs.nais.TicketSalesService.service.IEventService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/events")
public class EventController {

    private final IEventService eventService;

    public EventController(IEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<Event> getAll() {
        return eventService.findAll();
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getById(@PathVariable String eventId) {
        return eventService.findById(eventId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public List<Event> getByStatus(@PathVariable String status) {
        return eventService.findByStatus(status);
    }

    @PostMapping
    public ResponseEntity<Event> create(@RequestBody EventDTO dto) {
        return ResponseEntity.ok(eventService.create(dto));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Event> update(@PathVariable String eventId, @RequestBody EventDTO dto) {
        return eventService.update(eventId, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> delete(@PathVariable String eventId) {
        return eventService.delete(eventId) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{eventId}/zones")
    public ResponseEntity<Void> addZone(@PathVariable String eventId, @RequestBody EventZoneDTO dto) {
        return eventService.addZoneToEvent(eventId, dto) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{eventId}/zones")
    public ResponseEntity<Void> updateZone(@PathVariable String eventId, @RequestBody EventZoneDTO dto) {
        eventService.updateEventZone(eventId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{eventId}/zones/{zoneId}")
    public ResponseEntity<Void> removeZone(@PathVariable String eventId, @PathVariable String zoneId) {
        eventService.removeZoneFromEvent(eventId, zoneId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/analytics/revenue")
    public List<Map<String, Object>> getRevenue() {
        return eventService.getRevenueByEventAndZone();
    }

    @GetMapping("/analytics/occupancy")
    public List<Map<String, Object>> getOccupancy() {
        return eventService.getZoneOccupancyForUpcomingEvents();
    }

    @PutMapping("/{eventId}/dynamic-pricing")
    public List<Map<String, Object>> updateDynamicPricing(@PathVariable String eventId) {
        return eventService.updateDynamicPricing(eventId);
    }
}
