package rs.ac.uns.acs.nais.TicketSalesService.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketCancelledEvent {

    private String sagaId;
    private String customerId;
    private String seatId;

    public TicketCancelledEvent() {}

    public TicketCancelledEvent(String sagaId, String customerId, String seatId) {
        this.sagaId = sagaId;
        this.customerId = customerId;
        this.seatId = seatId;
    }

    public String getSagaId() { return sagaId; }
    public void setSagaId(String sagaId) { this.sagaId = sagaId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getSeatId() { return seatId; }
    public void setSeatId(String seatId) { this.seatId = seatId; }
}
