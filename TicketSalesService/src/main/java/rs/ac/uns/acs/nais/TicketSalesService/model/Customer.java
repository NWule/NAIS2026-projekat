package rs.ac.uns.acs.nais.TicketSalesService.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node
public class Customer {

    @Id
    private String customerId;

    private String name;
    private String surname;
    private String email;
    private String phone;
    private Integer loyaltyPoints;
    private Boolean isActive;

    @Relationship(type = "PURCHASED", direction = Relationship.Direction.OUTGOING)
    private List<Purchase> purchases = new ArrayList<>();

    public Customer() {}

    public Customer(String customerId, String name, String surname, String email,
                    String phone, Integer loyaltyPoints, Boolean isActive) {
        this.customerId = customerId;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.loyaltyPoints = loyaltyPoints;
        this.isActive = isActive;
    }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Integer getLoyaltyPoints() { return loyaltyPoints; }
    public void setLoyaltyPoints(Integer loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public List<Purchase> getPurchases() { return purchases; }
    public void setPurchases(List<Purchase> purchases) { this.purchases = purchases; }

    public void addPurchase(Purchase purchase) { this.purchases.add(purchase); }
}
