package rs.ac.uns.acs.nais.TicketSalesService.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import rs.ac.uns.acs.nais.TicketSalesService.config.RabbitMQConfig;
import rs.ac.uns.acs.nais.TicketSalesService.event.TicketCancelledEvent;
import rs.ac.uns.acs.nais.TicketSalesService.event.TicketPurchasedEvent;

@Component
public class TicketEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public TicketEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishTicketPurchased(TicketPurchasedEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "ticket.purchased", event);
    }

    public void publishTicketCancelled(TicketCancelledEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "ticket.cancelled", event);
    }
}
