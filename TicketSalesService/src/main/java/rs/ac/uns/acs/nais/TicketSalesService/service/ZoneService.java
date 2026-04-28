package rs.ac.uns.acs.nais.TicketSalesService.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.acs.nais.TicketSalesService.dto.ZoneDTO;
import rs.ac.uns.acs.nais.TicketSalesService.model.Zone;
import rs.ac.uns.acs.nais.TicketSalesService.repository.ZoneRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ZoneService implements IZoneService {

    private final ZoneRepository zoneRepository;

    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public List<Zone> findAll() {
        return zoneRepository.findAll();
    }

    public Optional<Zone> findById(String zoneId) {
        return zoneRepository.findById(zoneId);
    }

    public Zone create(ZoneDTO dto) {
        Zone zone = new Zone(dto.getZoneId(), dto.getName(), dto.getCapacity(),
                dto.getPriceMultiplier(), dto.getDescription());
        return zoneRepository.save(zone);
    }

    public Optional<Zone> update(String zoneId, ZoneDTO dto) {
        return zoneRepository.findById(zoneId).map(zone -> {
            zone.setName(dto.getName());
            zone.setCapacity(dto.getCapacity());
            zone.setPriceMultiplier(dto.getPriceMultiplier());
            zone.setDescription(dto.getDescription());
            return zoneRepository.save(zone);
        });
    }

    public boolean delete(String zoneId) {
        if (zoneRepository.existsById(zoneId)) {
            zoneRepository.deleteById(zoneId);
            return true;
        }
        return false;
    }
}
