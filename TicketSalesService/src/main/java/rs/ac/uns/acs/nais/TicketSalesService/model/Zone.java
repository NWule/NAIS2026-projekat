package rs.ac.uns.acs.nais.TicketSalesService.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class Zone {

    @Id
    private String zoneId;

    private String name;
    private Integer capacity;
    private Double priceMultiplier;
    private String description;

    public Zone() {}

    public Zone(String zoneId, String name, Integer capacity, Double priceMultiplier, String description) {
        this.zoneId = zoneId;
        this.name = name;
        this.capacity = capacity;
        this.priceMultiplier = priceMultiplier;
        this.description = description;
    }

    public String getZoneId() { return zoneId; }
    public void setZoneId(String zoneId) { this.zoneId = zoneId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Double getPriceMultiplier() { return priceMultiplier; }
    public void setPriceMultiplier(Double priceMultiplier) { this.priceMultiplier = priceMultiplier; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
