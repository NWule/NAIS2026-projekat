package rs.ac.uns.acs.nais.TicketSalesService.service;

import rs.ac.uns.acs.nais.TicketSalesService.dto.SeatDTO;
import rs.ac.uns.acs.nais.TicketSalesService.model.Seat;

import java.util.List;
import java.util.Optional;

public interface ISeatService {
    List<Seat> findAll();
    Optional<Seat> findById(String seatId);
    List<Seat> findByZone(String zoneId);
    Optional<Seat> create(SeatDTO dto);
    Optional<Seat> update(String seatId, SeatDTO dto);
    boolean delete(String seatId);
    void moveSeatToZone(String seatId, String newZoneId);
    void removeSeatFromZone(String seatId);
}
