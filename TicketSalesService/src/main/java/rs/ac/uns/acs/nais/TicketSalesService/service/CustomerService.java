package rs.ac.uns.acs.nais.TicketSalesService.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.acs.nais.TicketSalesService.dto.CustomerDTO;
import rs.ac.uns.acs.nais.TicketSalesService.dto.PurchaseTicketDTO;
import rs.ac.uns.acs.nais.TicketSalesService.event.TicketCancelledEvent;
import rs.ac.uns.acs.nais.TicketSalesService.event.TicketPurchasedEvent;
import rs.ac.uns.acs.nais.TicketSalesService.messaging.TicketEventPublisher;
import rs.ac.uns.acs.nais.TicketSalesService.model.Customer;
import rs.ac.uns.acs.nais.TicketSalesService.model.Event;
import rs.ac.uns.acs.nais.TicketSalesService.model.EventZone;
import rs.ac.uns.acs.nais.TicketSalesService.model.Seat;
import rs.ac.uns.acs.nais.TicketSalesService.repository.CustomerRepository;
import rs.ac.uns.acs.nais.TicketSalesService.repository.EventRepository;
import rs.ac.uns.acs.nais.TicketSalesService.repository.SeatRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final SeatRepository seatRepository;
    private final EventRepository eventRepository;
    private final TicketEventPublisher ticketEventPublisher;

    public CustomerService(CustomerRepository customerRepository,
                           SeatRepository seatRepository,
                           EventRepository eventRepository,
                           TicketEventPublisher ticketEventPublisher) {
        this.customerRepository = customerRepository;
        this.seatRepository = seatRepository;
        this.eventRepository = eventRepository;
        this.ticketEventPublisher = ticketEventPublisher;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(String customerId) {
        return customerRepository.findById(customerId);
    }

    @Override
    public Customer create(CustomerDTO dto) {
        Customer customer = new Customer(dto.getCustomerId(), dto.getName(), dto.getSurname(),
                dto.getEmail(), dto.getPhone(),
                dto.getLoyaltyPoints() != null ? dto.getLoyaltyPoints() : 0, true);
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> update(String customerId, CustomerDTO dto) {
        return customerRepository.findById(customerId).map(customer -> {
            customer.setName(dto.getName());
            customer.setSurname(dto.getSurname());
            customer.setEmail(dto.getEmail());
            customer.setPhone(dto.getPhone());
            return customerRepository.save(customer);
        });
    }

    @Override
    public boolean delete(String customerId) {
        return customerRepository.findById(customerId).map(customer -> {
            customer.setIsActive(false);
            customerRepository.save(customer);
            return true;
        }).orElse(false);
    }

    @Override
    public boolean purchaseTicket(PurchaseTicketDTO dto) {
        Optional<Customer> customerOpt = customerRepository.findById(dto.getCustomerId());
        Optional<Seat> seatOpt = seatRepository.findById(dto.getSeatId());
        if (customerOpt.isEmpty() || seatOpt.isEmpty()) return false;
        if (customerRepository.hasPurchasedSeat(dto.getCustomerId(), dto.getSeatId())) return false;

        Customer customer = customerOpt.get();
        Seat seat = seatOpt.get();
        double price = calculatePrice(seat);
        String purchaseDate = LocalDate.now().toString();

        customerRepository.createPurchase(dto.getCustomerId(), dto.getSeatId(),
                purchaseDate, price, dto.getTicketType(), 0.0, dto.getPaymentMethod());

        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + 10);
        customerRepository.save(customer);

        Optional<Map<String, Object>> eventData = eventRepository.findEventDataBySeatId(dto.getSeatId());

        TicketPurchasedEvent event = new TicketPurchasedEvent();
        event.setSagaId(UUID.randomUUID().toString());
        event.setCustomerId(customer.getCustomerId());
        event.setCustomerName(customer.getName() + " " + customer.getSurname());
        event.setCustomerEmail(customer.getEmail());
        event.setSeatId(seat.getSeatId());
        event.setSeatCategory(seat.getCategory());
        event.setPrice(price);
        event.setTicketType(dto.getTicketType());
        event.setDiscountPercent(0.0);
        event.setPaymentMethod(dto.getPaymentMethod());
        event.setPurchaseDate(purchaseDate);
        event.setLoyaltyPointsEarned(10);

        eventData.ifPresent(data -> {
            event.setEventId(data.get("eventId") != null ? data.get("eventId").toString() : null);
            event.setEventName(data.get("eventName") != null ? data.get("eventName").toString() : null);
            event.setEventDate(data.get("eventDate") != null ? data.get("eventDate").toString() : null);
            event.setEventType(data.get("eventType") != null ? data.get("eventType").toString() : null);
            event.setVenue(data.get("venue") != null ? data.get("venue").toString() : null);
            event.setZoneId(data.get("zoneId") != null ? data.get("zoneId").toString() : null);
            event.setZoneName(data.get("zoneName") != null ? data.get("zoneName").toString() : null);
        });

        ticketEventPublisher.publishTicketPurchased(event);
        return true;
    }

    private double calculatePrice(Seat seat) {
        String zoneId = seat.getZone() != null ? seat.getZone().getZoneId() : null;
        if (zoneId == null) return 100.0;
        for (Event event : eventRepository.findByStatus("UPCOMING")) {
            for (EventZone ez : event.getZones()) {
                if (ez.getZone() != null && ez.getZone().getZoneId().equals(zoneId)) {
                    return Math.round(event.getBasePrice()
                            * seat.getZone().getPriceMultiplier()
                            * ez.getDynamicPriceMultiplier() * 100.0) / 100.0;
                }
            }
        }
        return 100.0;
    }

    @Override
    public boolean cancelTicket(String customerId, String seatId) {
        if (!customerRepository.hasPurchasedSeat(customerId, seatId)) return false;
        customerRepository.cancelTicket(customerId, seatId);
        ticketEventPublisher.publishTicketCancelled(
                new TicketCancelledEvent(UUID.randomUUID().toString(), customerId, seatId));
        return true;
    }

    @Override
    public boolean updateDiscount(String customerId, String seatId, double discountPercent) {
        if (!customerRepository.hasPurchasedSeat(customerId, seatId)) return false;
        customerRepository.findById(customerId).ifPresent(customer ->
                customer.getPurchases().stream()
                        .filter(p -> p.getSeat() != null && p.getSeat().getSeatId().equals(seatId))
                        .findFirst()
                        .ifPresent(p -> {
                            double original = p.getPrice() / (1.0 - p.getDiscountPercent() / 100.0);
                            double newPrice = Math.round(original * (1.0 - discountPercent / 100.0) * 100.0) / 100.0;
                            customerRepository.updatePurchaseDiscount(customerId, seatId, discountPercent, newPrice);
                        }));
        return true;
    }

    @Override
    public List<Map<String, Object>> getTopCustomersBySpending() {
        return customerRepository.getTopCustomersBySpending();
    }

    @Override
    public Long applyLoyaltyDiscounts(int minPoints) {
        return customerRepository.applyLoyaltyDiscounts(minPoints);
    }
}
