package rs.ac.uns.acs.nais.TicketSalesService.repository;

import java.util.List;
import java.util.Map;

public interface CustomerRepositoryCustom {
    List<Map<String, Object>> getTopCustomersBySpending();
    Long applyLoyaltyDiscounts(int minPoints);
}
