package rs.ac.uns.acs.nais.TicketSalesService.dto;

public class EventZoneDTO {
    private String zoneId;
    private Integer availableSeats;
    private Integer soldSeats;
    private Double dynamicPriceMultiplier;

    public EventZoneDTO() {}

    public String getZoneId() { return zoneId; }
    public void setZoneId(String zoneId) { this.zoneId = zoneId; }

    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }

    public Integer getSoldSeats() { return soldSeats; }
    public void setSoldSeats(Integer soldSeats) { this.soldSeats = soldSeats; }

    public Double getDynamicPriceMultiplier() { return dynamicPriceMultiplier; }
    public void setDynamicPriceMultiplier(Double dynamicPriceMultiplier) { this.dynamicPriceMultiplier = dynamicPriceMultiplier; }
}
