package rs.ac.uns.acs.nais.TicketSearchService.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketPurchasedEvent {

    private String sagaId;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String eventId;
    private String eventName;
    private String eventDate;
    private String eventType;
    private String venue;
    private String zoneId;
    private String zoneName;
    private String seatId;
    private String seatCategory;
    private Double price;
    private String ticketType;
    private Double discountPercent;
    private String paymentMethod;
    private String purchaseDate;
    private Integer loyaltyPointsEarned;

    public TicketPurchasedEvent() {}

    public String getSagaId() { return sagaId; }
    public void setSagaId(String sagaId) { this.sagaId = sagaId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public String getZoneId() { return zoneId; }
    public void setZoneId(String zoneId) { this.zoneId = zoneId; }
    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }
    public String getSeatId() { return seatId; }
    public void setSeatId(String seatId) { this.seatId = seatId; }
    public String getSeatCategory() { return seatCategory; }
    public void setSeatCategory(String seatCategory) { this.seatCategory = seatCategory; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getTicketType() { return ticketType; }
    public void setTicketType(String ticketType) { this.ticketType = ticketType; }
    public Double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(Double discountPercent) { this.discountPercent = discountPercent; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(String purchaseDate) { this.purchaseDate = purchaseDate; }
    public Integer getLoyaltyPointsEarned() { return loyaltyPointsEarned; }
    public void setLoyaltyPointsEarned(Integer loyaltyPointsEarned) { this.loyaltyPointsEarned = loyaltyPointsEarned; }
}
