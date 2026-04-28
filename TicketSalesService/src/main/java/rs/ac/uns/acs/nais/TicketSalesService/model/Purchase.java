package rs.ac.uns.acs.nais.TicketSalesService.model;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.time.LocalDate;

@RelationshipProperties
public class Purchase {

    @RelationshipId
    private Long id;

    private LocalDate purchaseDate;
    private Double price;
    private String ticketType;
    private Double discountPercent;
    private String paymentMethod;

    @TargetNode
    private Seat seat;

    public Purchase() {}

    public Purchase(Seat seat, LocalDate purchaseDate, Double price,
                    String ticketType, Double discountPercent, String paymentMethod) {
        this.seat = seat;
        this.purchaseDate = purchaseDate;
        this.price = price;
        this.ticketType = ticketType;
        this.discountPercent = discountPercent;
        this.paymentMethod = paymentMethod;
    }

    public Long getId() { return id; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getTicketType() { return ticketType; }
    public void setTicketType(String ticketType) { this.ticketType = ticketType; }

    public Double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(Double discountPercent) { this.discountPercent = discountPercent; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Seat getSeat() { return seat; }
    public void setSeat(Seat seat) { this.seat = seat; }
}
