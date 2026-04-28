package rs.ac.uns.acs.nais.TicketSalesService.repository;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class CustomerRepositoryCustomImpl implements CustomerRepositoryCustom {

    private final Neo4jClient neo4jClient;

    public CustomerRepositoryCustomImpl(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    @Override
    public List<Map<String, Object>> getTopCustomersBySpending() {
        return new ArrayList<>(neo4jClient.query(
                "MATCH (c:Customer)-[p:PURCHASED]->(s:Seat)-[:IN_ZONE]->(z:Zone)<-[:HAS_ZONE]-(e:Event) " +
                "WHERE c.isActive = true " +
                "WITH c, COUNT(p) AS totalPurchases, SUM(p.price) AS totalSpent " +
                "WHERE totalPurchases > 0 " +
                "RETURN c.customerId AS customerId, c.name AS name, c.surname AS surname, " +
                "c.email AS email, c.loyaltyPoints AS loyaltyPoints, " +
                "totalPurchases, totalSpent " +
                "ORDER BY totalSpent DESC " +
                "LIMIT 10"
        ).fetch().all());
    }

    @Override
    public Long applyLoyaltyDiscounts(int minPoints) {
        return neo4jClient.query(
                "MATCH (c:Customer)-[p:PURCHASED]->(s:Seat)-[:IN_ZONE]->(z:Zone)<-[:HAS_ZONE]-(e:Event) " +
                "WHERE e.status = 'UPCOMING' AND c.loyaltyPoints >= $minPoints AND p.discountPercent = 0.0 " +
                "WITH c, p, " +
                "CASE WHEN c.loyaltyPoints >= 500 THEN 15.0 " +
                "     WHEN c.loyaltyPoints >= 200 THEN 10.0 " +
                "     ELSE 5.0 END AS discount " +
                "SET p.discountPercent = discount, p.price = p.price * (1.0 - discount / 100.0) " +
                "RETURN COUNT(p) AS updatedTickets"
        ).bindAll(Map.of("minPoints", minPoints))
         .fetchAs(Long.class)
         .mappedBy((typeSystem, record) -> record.get("updatedTickets").asLong())
         .one()
         .orElse(0L);
    }
}
