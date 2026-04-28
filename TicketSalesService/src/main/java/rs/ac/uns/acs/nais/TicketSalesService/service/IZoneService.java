package rs.ac.uns.acs.nais.TicketSalesService.service;

import rs.ac.uns.acs.nais.TicketSalesService.dto.ZoneDTO;
import rs.ac.uns.acs.nais.TicketSalesService.model.Zone;

import java.util.List;
import java.util.Optional;

public interface IZoneService {
    List<Zone> findAll();
    Optional<Zone> findById(String zoneId);
    Zone create(ZoneDTO dto);
    Optional<Zone> update(String zoneId, ZoneDTO dto);
    boolean delete(String zoneId);
}
