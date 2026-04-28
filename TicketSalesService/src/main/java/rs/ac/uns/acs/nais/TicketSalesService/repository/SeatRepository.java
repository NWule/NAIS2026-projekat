package rs.ac.uns.acs.nais.TicketSalesService.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.uns.acs.nais.TicketSalesService.model.Seat;

import java.util.List;

@Repository
public interface SeatRepository extends Neo4jRepository<Seat, String> {

    @Query("MATCH (s:Seat)-[:IN_ZONE]->(z:Zone {zoneId: $zoneId}) RETURN s, [(s)-[:IN_ZONE]->(z2) | z2]")
    List<Seat> findByZoneId(@Param("zoneId") String zoneId);

    @Query("MATCH (s:Seat {seatId: $seatId})-[r:IN_ZONE]->(:Zone) DELETE r " +
           "WITH s MATCH (z:Zone {zoneId: $newZoneId}) CREATE (s)-[:IN_ZONE]->(z)")
    void moveSeatToZone(@Param("seatId") String seatId, @Param("newZoneId") String newZoneId);

    @Query("MATCH (s:Seat {seatId: $seatId})-[r:IN_ZONE]->(:Zone) DELETE r")
    void removeSeatFromZone(@Param("seatId") String seatId);
}
