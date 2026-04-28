package rs.ac.uns.acs.nais.TicketSalesService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.acs.nais.TicketSalesService.dto.CustomerDTO;
import rs.ac.uns.acs.nais.TicketSalesService.dto.PurchaseTicketDTO;
import rs.ac.uns.acs.nais.TicketSalesService.model.Customer;
import rs.ac.uns.acs.nais.TicketSalesService.service.ICustomerService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final ICustomerService customerService;

    public CustomerController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getAll() {
        return customerService.findAll();
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getById(@PathVariable String customerId) {
        return customerService.findById(customerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody CustomerDTO dto) {
        return ResponseEntity.ok(customerService.create(dto));
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> update(@PathVariable String customerId, @RequestBody CustomerDTO dto) {
        return customerService.update(customerId, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> delete(@PathVariable String customerId) {
        return customerService.delete(customerId) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/tickets/purchase")
    public ResponseEntity<Void> purchaseTicket(@RequestBody PurchaseTicketDTO dto) {
        return customerService.purchaseTicket(dto) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{customerId}/tickets/{seatId}")
    public ResponseEntity<Void> cancelTicket(@PathVariable String customerId, @PathVariable String seatId) {
        return customerService.cancelTicket(customerId, seatId) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{customerId}/tickets/{seatId}/discount")
    public ResponseEntity<Void> updateDiscount(@PathVariable String customerId,
                                               @PathVariable String seatId,
                                               @RequestParam double discountPercent) {
        return customerService.updateDiscount(customerId, seatId, discountPercent)
                ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/analytics/top")
    public List<Map<String, Object>> getTopCustomers() {
        return customerService.getTopCustomersBySpending();
    }

    @PutMapping("/analytics/loyalty-discounts")
    public ResponseEntity<Long> applyLoyaltyDiscounts(@RequestParam(defaultValue = "100") int minPoints) {
        return ResponseEntity.ok(customerService.applyLoyaltyDiscounts(minPoints));
    }
}
