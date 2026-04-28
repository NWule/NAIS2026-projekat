package rs.ac.uns.acs.nais.TicketSalesService.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.acs.nais.TicketSalesService.dto.SeatDTO;
import rs.ac.uns.acs.nais.TicketSalesService.model.Seat;
import rs.ac.uns.acs.nais.TicketSalesService.model.Zone;
import rs.ac.uns.acs.nais.TicketSalesService.repository.SeatRepository;
import rs.ac.uns.acs.nais.TicketSalesService.repository.ZoneRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService implements ISeatService {

    private final SeatRepository seatRepository;
    private final ZoneRepository zoneRepository;

    public SeatService(SeatRepository seatRepository, ZoneRepository zoneRepository) {
        this.seatRepository = seatRepository;
        this.zoneRepository = zoneRepository;
    }

    public List<Seat> findAll() {
        return seatRepository.findAll();
    }

    public Optional<Seat> findById(String seatId) {
        return seatRepository.findById(seatId);
    }

    public List<Seat> findByZone(String zoneId) {
        return seatRepository.findByZoneId(zoneId);
    }

    public Optional<Seat> create(SeatDTO dto) {
        Optional<Zone> zone = zoneRepository.findById(dto.getZoneId());
        if (zone.isEmpty()) return Optional.empty();
        Seat seat = new Seat(dto.getSeatId(), dto.getSeatRow(), dto.getSeatNumber(),
                dto.getCategory(), zone.get());
        return Optional.of(seatRepository.save(seat));
    }

    public Optional<Seat> update(String seatId, SeatDTO dto) {
        return seatRepository.findById(seatId).map(seat -> {
            seat.setSeatRow(dto.getSeatRow());
            seat.setSeatNumber(dto.getSeatNumber());
            seat.setCategory(dto.getCategory());
            return seatRepository.save(seat);
        });
    }

    public boolean delete(String seatId) {
        if (seatRepository.existsById(seatId)) {
            seatRepository.deleteById(seatId);
            return true;
        }
        return false;
    }

    public void moveSeatToZone(String seatId, String newZoneId) {
        seatRepository.moveSeatToZone(seatId, newZoneId);
    }

    public void removeSeatFromZone(String seatId) {
        seatRepository.removeSeatFromZone(seatId);
    }
}
