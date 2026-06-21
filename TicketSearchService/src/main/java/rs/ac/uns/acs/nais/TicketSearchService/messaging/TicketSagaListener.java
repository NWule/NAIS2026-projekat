package rs.ac.uns.acs.nais.TicketSearchService.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import rs.ac.uns.acs.nais.TicketSearchService.config.RabbitMQConfig;
import rs.ac.uns.acs.nais.TicketSearchService.event.TicketCancelledEvent;
import rs.ac.uns.acs.nais.TicketSearchService.event.TicketPurchasedEvent;
import rs.ac.uns.acs.nais.TicketSearchService.event.TicketPurchaseRollbackEvent;
import rs.ac.uns.acs.nais.TicketSearchService.model.elastic.TicketSaleDocument;
import rs.ac.uns.acs.nais.TicketSearchService.repository.elastic.TicketSaleElasticRepository;

import java.util.List;
import java.util.UUID;

@Component
public class TicketSagaListener {

    private final TicketSaleElasticRepository ticketSaleRepository;
    private final RabbitTemplate rabbitTemplate;

    public TicketSagaListener(TicketSaleElasticRepository ticketSaleRepository,
                               RabbitTemplate rabbitTemplate) {
        this.ticketSaleRepository = ticketSaleRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.PURCHASED_QUEUE)
    public void handleTicketPurchased(TicketPurchasedEvent event) {
        try {
            TicketSaleDocument doc = new TicketSaleDocument();
            doc.setId(UUID.randomUUID().toString());
            doc.setCustomerId(event.getCustomerId());
            doc.setCustomerName(event.getCustomerName());
            doc.setCustomerEmail(event.getCustomerEmail());
            doc.setEventId(event.getEventId());
            doc.setEventName(event.getEventName());
            doc.setEventDate(event.getEventDate());
            doc.setEventType(event.getEventType());
            doc.setVenue(event.getVenue());
            doc.setZoneId(event.getZoneId());
            doc.setZoneName(event.getZoneName());
            doc.setSeatId(event.getSeatId());
            doc.setSeatCategory(event.getSeatCategory());
            doc.setPrice(event.getPrice());
            doc.setTicketType(event.getTicketType());
            doc.setDiscountPercent(event.getDiscountPercent());
            doc.setPaymentMethod(event.getPaymentMethod());
            doc.setPurchaseDate(event.getPurchaseDate());
            doc.setLoyaltyPointsEarned(event.getLoyaltyPointsEarned());
            ticketSaleRepository.save(doc);
        } catch (Exception e) {
            TicketPurchaseRollbackEvent rollback = new TicketPurchaseRollbackEvent();
            rollback.setSagaId(event.getSagaId());
            rollback.setCustomerId(event.getCustomerId());
            rollback.setSeatId(event.getSeatId());
            rollback.setReason(e.getMessage());
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "ticket.rollback", rollback);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.CANCELLED_QUEUE)
    public void handleTicketCancelled(TicketCancelledEvent event) {
        List<TicketSaleDocument> docs = ticketSaleRepository.findBySeatId(event.getSeatId());
        docs.forEach(doc -> ticketSaleRepository.deleteById(doc.getId()));
    }
}
