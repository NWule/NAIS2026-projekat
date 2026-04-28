package rs.ac.uns.acs.nais.TicketSalesService.dto;

public class PurchaseTicketDTO {
    private String customerId;
    private String seatId;
    private String ticketType;
    private String paymentMethod;

    public PurchaseTicketDTO() {}

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getSeatId() { return seatId; }
    public void setSeatId(String seatId) { this.seatId = seatId; }

    public String getTicketType() { return ticketType; }
    public void setTicketType(String ticketType) { this.ticketType = ticketType; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
