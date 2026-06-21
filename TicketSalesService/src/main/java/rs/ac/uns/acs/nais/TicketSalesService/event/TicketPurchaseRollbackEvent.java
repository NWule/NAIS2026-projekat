package rs.ac.uns.acs.nais.TicketSalesService.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketPurchaseRollbackEvent {

    private String sagaId;
    private String customerId;
    private String seatId;
    private String reason;

    public TicketPurchaseRollbackEvent() {}

    public String getSagaId() { return sagaId; }
    public void setSagaId(String sagaId) { this.sagaId = sagaId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getSeatId() { return seatId; }
    public void setSeatId(String seatId) { this.seatId = seatId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
