package rs.ac.uns.acs.nais.TicketSalesService.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.uns.acs.nais.TicketSalesService.model.Event;

import java.util.List;

@Repository
public interface EventRepository extends Neo4jRepository<Event, String>, EventRepositoryCustom {

    List<Event> findByStatus(String status);

    List<Event> findByEventType(String eventType);

    @Query("MATCH (e:Event {eventId: $eventId}) " +
           "MATCH (z:Zone {zoneId: $zoneId}) " +
           "CREATE (e)-[:HAS_ZONE {availableSeats: $availableSeats, soldSeats: 0, dynamicPriceMultiplier: 1.0}]->(z)")
    void addZoneToEvent(@Param("eventId") String eventId,
                        @Param("zoneId") String zoneId,
                        @Param("availableSeats") int availableSeats);

    @Query("MATCH (e:Event {eventId: $eventId})-[hz:HAS_ZONE]->(z:Zone {zoneId: $zoneId}) " +
           "SET hz.availableSeats = $availableSeats, hz.soldSeats = $soldSeats, " +
           "hz.dynamicPriceMultiplier = $multiplier")
    void updateEventZone(@Param("eventId") String eventId,
                         @Param("zoneId") String zoneId,
                         @Param("availableSeats") int availableSeats,
                         @Param("soldSeats") int soldSeats,
                         @Param("multiplier") double multiplier);

    @Query("MATCH (e:Event {eventId: $eventId})-[hz:HAS_ZONE]->(z:Zone {zoneId: $zoneId}) DELETE hz")
    void removeZoneFromEvent(@Param("eventId") String eventId, @Param("zoneId") String zoneId);
}
