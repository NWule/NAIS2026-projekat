package rs.ac.uns.acs.nais.TicketSalesService.service;

import rs.ac.uns.acs.nais.TicketSalesService.dto.EventDTO;
import rs.ac.uns.acs.nais.TicketSalesService.dto.EventZoneDTO;
import rs.ac.uns.acs.nais.TicketSalesService.model.Event;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IEventService {
    List<Event> findAll();
    Optional<Event> findById(String eventId);
    List<Event> findByStatus(String status);
    Event create(EventDTO dto);
    Optional<Event> update(String eventId, EventDTO dto);
    boolean delete(String eventId);
    boolean addZoneToEvent(String eventId, EventZoneDTO dto);
    void updateEventZone(String eventId, EventZoneDTO dto);
    void removeZoneFromEvent(String eventId, String zoneId);
    List<Map<String, Object>> getRevenueByEventAndZone();
    List<Map<String, Object>> getZoneOccupancyForUpcomingEvents();
    List<Map<String, Object>> updateDynamicPricing(String eventId);
}
