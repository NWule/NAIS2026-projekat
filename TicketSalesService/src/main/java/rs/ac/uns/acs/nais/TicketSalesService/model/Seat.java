package rs.ac.uns.acs.nais.TicketSalesService.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
public class Seat {

    @Id
    private String seatId;

    private String seatRow;
    private String seatNumber;
    private String category;

    @Relationship(type = "IN_ZONE", direction = Relationship.Direction.OUTGOING)
    private Zone zone;

    public Seat() {}

    public Seat(String seatId, String seatRow, String seatNumber, String category, Zone zone) {
        this.seatId = seatId;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.category = category;
        this.zone = zone;
    }

    public String getSeatId() { return seatId; }
    public void setSeatId(String seatId) { this.seatId = seatId; }

    public String getSeatRow() { return seatRow; }
    public void setSeatRow(String seatRow) { this.seatRow = seatRow; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Zone getZone() { return zone; }
    public void setZone(Zone zone) { this.zone = zone; }
}
