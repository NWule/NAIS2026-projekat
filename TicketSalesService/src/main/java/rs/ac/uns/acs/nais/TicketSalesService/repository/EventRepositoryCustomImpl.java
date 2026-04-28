package rs.ac.uns.acs.nais.TicketSalesService.repository;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class EventRepositoryCustomImpl implements EventRepositoryCustom {

    private final Neo4jClient neo4jClient;

    public EventRepositoryCustomImpl(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    @Override
    public List<Map<String, Object>> getRevenueByEventAndZone() {
        return new ArrayList<>(neo4jClient.query(
                "MATCH (c:Customer)-[p:PURCHASED]->(s:Seat)-[:IN_ZONE]->(z:Zone)<-[:HAS_ZONE]-(e:Event) " +
                "WHERE e.status IN ['COMPLETED', 'ONGOING'] " +
                "WITH e.name AS eventName, z.name AS zoneName, " +
                "COUNT(p) AS ticketsSold, SUM(p.price) AS totalRevenue, AVG(p.discountPercent) AS avgDiscount " +
                "RETURN eventName, zoneName, ticketsSold, totalRevenue, avgDiscount " +
                "ORDER BY totalRevenue DESC"
        ).fetch().all());
    }

    @Override
    public List<Map<String, Object>> getZoneOccupancyForUpcomingEvents() {
        return new ArrayList<>(neo4jClient.query(
                "MATCH (e:Event)-[hz:HAS_ZONE]->(z:Zone) " +
                "WHERE e.status = 'UPCOMING' " +
                "WITH e.name AS eventName, e.date AS eventDate, " +
                "COUNT(z) AS numberOfZones, " +
                "SUM(hz.availableSeats + hz.soldSeats) AS totalCapacity, " +
                "SUM(hz.soldSeats) AS totalSold, " +
                "AVG(z.priceMultiplier * hz.dynamicPriceMultiplier) AS avgPriceMultiplier " +
                "RETURN eventName, eventDate, numberOfZones, totalCapacity, totalSold, " +
                "ROUND(100.0 * totalSold / totalCapacity, 2) AS occupancyPercent, avgPriceMultiplier " +
                "ORDER BY occupancyPercent DESC"
        ).fetch().all());
    }

    @Override
    public List<Map<String, Object>> updateDynamicPricing(String eventId) {
        return new ArrayList<>(neo4jClient.query(
                "MATCH (e:Event {eventId: $eventId})-[hz:HAS_ZONE]->(z:Zone) " +
                "WHERE hz.availableSeats + hz.soldSeats > 0 " +
                "WITH e, hz, z, toFloat(hz.soldSeats) / (hz.availableSeats + hz.soldSeats) AS occupancyRate " +
                "WITH e, hz, z, occupancyRate, " +
                "CASE WHEN occupancyRate >= 0.9 THEN 2.0 " +
                "     WHEN occupancyRate >= 0.7 THEN 1.5 " +
                "     WHEN occupancyRate >= 0.5 THEN 1.2 " +
                "     ELSE 1.0 END AS newMultiplier " +
                "SET hz.dynamicPriceMultiplier = newMultiplier " +
                "RETURN z.name AS zoneName, ROUND(occupancyRate * 100, 2) AS occupancyPercent, newMultiplier"
        ).bindAll(Map.of("eventId", eventId))
         .fetch().all());
    }
}
