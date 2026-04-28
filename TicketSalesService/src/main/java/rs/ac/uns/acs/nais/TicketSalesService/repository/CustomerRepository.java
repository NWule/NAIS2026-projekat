package rs.ac.uns.acs.nais.TicketSalesService.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.uns.acs.nais.TicketSalesService.model.Customer;

@Repository
public interface CustomerRepository extends Neo4jRepository<Customer, String>, CustomerRepositoryCustom {

    Customer findByEmail(String email);

    @Query("MATCH (c:Customer {customerId: $customerId})-[r:PURCHASED]->(s:Seat {seatId: $seatId}) " +
           "RETURN count(r) > 0")
    boolean hasPurchasedSeat(@Param("customerId") String customerId, @Param("seatId") String seatId);

    @Query("MATCH (c:Customer {customerId: $customerId}) " +
           "MATCH (s:Seat {seatId: $seatId}) " +
           "CREATE (c)-[p:PURCHASED {purchaseDate: date($purchaseDate), price: $price, " +
           "ticketType: $ticketType, discountPercent: $discountPercent, paymentMethod: $paymentMethod}]->(s)")
    void createPurchase(@Param("customerId") String customerId,
                        @Param("seatId") String seatId,
                        @Param("purchaseDate") String purchaseDate,
                        @Param("price") double price,
                        @Param("ticketType") String ticketType,
                        @Param("discountPercent") double discountPercent,
                        @Param("paymentMethod") String paymentMethod);

    @Query("MATCH (c:Customer {customerId: $customerId})-[p:PURCHASED]->(s:Seat {seatId: $seatId}) " +
           "SET p.discountPercent = $discountPercent, p.price = $newPrice")
    void updatePurchaseDiscount(@Param("customerId") String customerId,
                                @Param("seatId") String seatId,
                                @Param("discountPercent") double discountPercent,
                                @Param("newPrice") double newPrice);

    @Query("MATCH (c:Customer {customerId: $customerId})-[p:PURCHASED]->(s:Seat {seatId: $seatId}) DELETE p")
    void cancelTicket(@Param("customerId") String customerId, @Param("seatId") String seatId);
}
