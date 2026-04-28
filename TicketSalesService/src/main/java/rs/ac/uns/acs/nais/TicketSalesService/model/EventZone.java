package rs.ac.uns.acs.nais.TicketSalesService.model;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
public class EventZone {

    @RelationshipId
    private Long id;

    private Integer availableSeats;
    private Integer soldSeats;
    private Double dynamicPriceMultiplier;

    @TargetNode
    private Zone zone;

    public EventZone() {}

    public EventZone(Zone zone, Integer availableSeats, Integer soldSeats, Double dynamicPriceMultiplier) {
        this.zone = zone;
        this.availableSeats = availableSeats;
        this.soldSeats = soldSeats;
        this.dynamicPriceMultiplier = dynamicPriceMultiplier;
    }

    public Long getId() { return id; }

    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }

    public Integer getSoldSeats() { return soldSeats; }
    public void setSoldSeats(Integer soldSeats) { this.soldSeats = soldSeats; }

    public Double getDynamicPriceMultiplier() { return dynamicPriceMultiplier; }
    public void setDynamicPriceMultiplier(Double dynamicPriceMultiplier) { this.dynamicPriceMultiplier = dynamicPriceMultiplier; }

    public Zone getZone() { return zone; }
    public void setZone(Zone zone) { this.zone = zone; }
}
