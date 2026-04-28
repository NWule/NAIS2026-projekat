package rs.ac.uns.acs.nais.TicketSalesService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.acs.nais.TicketSalesService.dto.ZoneDTO;
import rs.ac.uns.acs.nais.TicketSalesService.model.Zone;
import rs.ac.uns.acs.nais.TicketSalesService.service.IZoneService;

import java.util.List;

@RestController
@RequestMapping("/zones")
public class ZoneController {

    private final IZoneService zoneService;

    public ZoneController(IZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @GetMapping
    public List<Zone> getAll() {
        return zoneService.findAll();
    }

    @GetMapping("/{zoneId}")
    public ResponseEntity<Zone> getById(@PathVariable String zoneId) {
        return zoneService.findById(zoneId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Zone> create(@RequestBody ZoneDTO dto) {
        return ResponseEntity.ok(zoneService.create(dto));
    }

    @PutMapping("/{zoneId}")
    public ResponseEntity<Zone> update(@PathVariable String zoneId, @RequestBody ZoneDTO dto) {
        return zoneService.update(zoneId, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{zoneId}")
    public ResponseEntity<Void> delete(@PathVariable String zoneId) {
        return zoneService.delete(zoneId) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
