package rs.ac.uns.acs.nais.TicketSalesService.dto;

public class ZoneDTO {
    private String zoneId;
    private String name;
    private Integer capacity;
    private Double priceMultiplier;
    private String description;

    public ZoneDTO() {}

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
