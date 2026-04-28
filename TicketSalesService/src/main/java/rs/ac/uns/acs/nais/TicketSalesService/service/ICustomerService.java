package rs.ac.uns.acs.nais.TicketSalesService.service;

import rs.ac.uns.acs.nais.TicketSalesService.dto.CustomerDTO;
import rs.ac.uns.acs.nais.TicketSalesService.dto.PurchaseTicketDTO;
import rs.ac.uns.acs.nais.TicketSalesService.model.Customer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ICustomerService {
    List<Customer> findAll();
    Optional<Customer> findById(String customerId);
    Customer create(CustomerDTO dto);
    Optional<Customer> update(String customerId, CustomerDTO dto);
    boolean delete(String customerId);
    boolean purchaseTicket(PurchaseTicketDTO dto);
    boolean cancelTicket(String customerId, String seatId);
    boolean updateDiscount(String customerId, String seatId, double discountPercent);
    List<Map<String, Object>> getTopCustomersBySpending();
    Long applyLoyaltyDiscounts(int minPoints);
}
