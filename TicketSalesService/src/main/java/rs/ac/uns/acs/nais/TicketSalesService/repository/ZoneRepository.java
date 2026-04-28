package rs.ac.uns.acs.nais.TicketSalesService.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.acs.nais.TicketSalesService.model.Zone;

@Repository
public interface ZoneRepository extends Neo4jRepository<Zone, String> {
}
