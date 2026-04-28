package rs.ac.uns.acs.nais.TicketSalesService.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Node
public class Event {

    @Id
    private String eventId;

    private String name;
    private LocalDate date;
    private String venue;
    private String eventType;  
    private String status;      
    private Double basePrice;

    @Relationship(type = "HAS_ZONE", direction = Relationship.Direction.OUTGOING)
    private List<EventZone> zones = new ArrayList<>();

    public Event() {}

    public Event(String eventId, String name, LocalDate date, String venue,
                 String eventType, String status, Double basePrice) {
        this.eventId = eventId;
        this.name = name;
        this.date = date;
        this.venue = venue;
        this.eventType = eventType;
        this.status = status;
        this.basePrice = basePrice;
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getBasePrice() { return basePrice; }
    public void setBasePrice(Double basePrice) { this.basePrice = basePrice; }

    public List<EventZone> getZones() { return zones; }
    public void setZones(List<EventZone> zones) { this.zones = zones; }

    public void addZone(EventZone eventZone) { this.zones.add(eventZone); }
}
