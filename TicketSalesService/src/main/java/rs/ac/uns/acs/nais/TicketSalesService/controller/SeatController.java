package rs.ac.uns.acs.nais.TicketSalesService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.acs.nais.TicketSalesService.dto.SeatDTO;
import rs.ac.uns.acs.nais.TicketSalesService.model.Seat;
import rs.ac.uns.acs.nais.TicketSalesService.service.ISeatService;

import java.util.List;

@RestController
@RequestMapping("/seats")
public class SeatController {

    private final ISeatService seatService;

    public SeatController(ISeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping
    public List<Seat> getAll() {
        return seatService.findAll();
    }

    @GetMapping("/{seatId}")
    public ResponseEntity<Seat> getById(@PathVariable String seatId) {
        return seatService.findById(seatId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/zone/{zoneId}")
    public List<Seat> getByZone(@PathVariable String zoneId) {
        return seatService.findByZone(zoneId);
    }

    @PostMapping
    public ResponseEntity<Seat> create(@RequestBody SeatDTO dto) {
        return seatService.create(dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{seatId}")
    public ResponseEntity<Seat> update(@PathVariable String seatId, @RequestBody SeatDTO dto) {
        return seatService.update(seatId, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{seatId}/zone/{newZoneId}")
    public ResponseEntity<Void> moveSeatToZone(@PathVariable String seatId, @PathVariable String newZoneId) {
        seatService.moveSeatToZone(seatId, newZoneId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{seatId}/zone")
    public ResponseEntity<Void> removeSeatFromZone(@PathVariable String seatId) {
        seatService.removeSeatFromZone(seatId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{seatId}")
    public ResponseEntity<Void> delete(@PathVariable String seatId) {
        return seatService.delete(seatId) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
