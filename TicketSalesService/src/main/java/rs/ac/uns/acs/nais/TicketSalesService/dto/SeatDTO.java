package rs.ac.uns.acs.nais.TicketSalesService.dto;

public class SeatDTO {
    private String seatId;
    private String seatRow;
    private String seatNumber;
    private String category;
    private String zoneId;

    public SeatDTO() {}

    public String getSeatId() { return seatId; }
    public void setSeatId(String seatId) { this.seatId = seatId; }

    public String getSeatRow() { return seatRow; }
    public void setSeatRow(String seatRow) { this.seatRow = seatRow; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getZoneId() { return zoneId; }
    public void setZoneId(String zoneId) { this.zoneId = zoneId; }
}
