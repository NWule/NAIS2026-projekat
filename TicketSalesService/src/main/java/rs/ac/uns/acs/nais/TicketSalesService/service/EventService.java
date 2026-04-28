package rs.ac.uns.acs.nais.TicketSalesService.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.acs.nais.TicketSalesService.dto.EventDTO;
import rs.ac.uns.acs.nais.TicketSalesService.dto.EventZoneDTO;
import rs.ac.uns.acs.nais.TicketSalesService.model.Event;
import rs.ac.uns.acs.nais.TicketSalesService.repository.EventRepository;
import rs.ac.uns.acs.nais.TicketSalesService.repository.ZoneRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EventService implements IEventService {

    private final EventRepository eventRepository;
    private final ZoneRepository zoneRepository;

    public EventService(EventRepository eventRepository, ZoneRepository zoneRepository) {
        this.eventRepository = eventRepository;
        this.zoneRepository = zoneRepository;
    }

    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public Optional<Event> findById(String eventId) {
        return eventRepository.findById(eventId);
    }

    @Override
    public List<Event> findByStatus(String status) {
        return eventRepository.findByStatus(status);
    }

    @Override
    public Event create(EventDTO dto) {
        Event event = new Event(dto.getEventId(), dto.getName(), dto.getDate(),
                dto.getVenue(), dto.getEventType(), dto.getStatus(), dto.getBasePrice());
        return eventRepository.save(event);
    }

    @Override
    public Optional<Event> update(String eventId, EventDTO dto) {
        return eventRepository.findById(eventId).map(event -> {
            event.setName(dto.getName());
            event.setDate(dto.getDate());
            event.setVenue(dto.getVenue());
            event.setEventType(dto.getEventType());
            event.setStatus(dto.getStatus());
            event.setBasePrice(dto.getBasePrice());
            return eventRepository.save(event);
        });
    }

    @Override
    public boolean delete(String eventId) {
        if (eventRepository.existsById(eventId)) {
            eventRepository.deleteById(eventId);
            return true;
        }
        return false;
    }

    @Override
    public boolean addZoneToEvent(String eventId, EventZoneDTO dto) {
        if (!eventRepository.existsById(eventId) || !zoneRepository.existsById(dto.getZoneId()))
            return false;
        eventRepository.addZoneToEvent(eventId, dto.getZoneId(), dto.getAvailableSeats());
        return true;
    }

    @Override
    public void updateEventZone(String eventId, EventZoneDTO dto) {
        eventRepository.updateEventZone(eventId, dto.getZoneId(),
                dto.getAvailableSeats(), dto.getSoldSeats(), dto.getDynamicPriceMultiplier());
    }

    @Override
    public void removeZoneFromEvent(String eventId, String zoneId) {
        eventRepository.removeZoneFromEvent(eventId, zoneId);
    }

    @Override
    public List<Map<String, Object>> getRevenueByEventAndZone() {
        return eventRepository.getRevenueByEventAndZone();
    }

    @Override
    public List<Map<String, Object>> getZoneOccupancyForUpcomingEvents() {
        return eventRepository.getZoneOccupancyForUpcomingEvents();
    }

    @Override
    public List<Map<String, Object>> updateDynamicPricing(String eventId) {
        return eventRepository.updateDynamicPricing(eventId);
    }
}
