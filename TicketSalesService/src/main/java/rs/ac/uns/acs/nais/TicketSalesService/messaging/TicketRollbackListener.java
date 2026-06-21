package rs.ac.uns.acs.nais.TicketSalesService.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import rs.ac.uns.acs.nais.TicketSalesService.config.RabbitMQConfig;
import rs.ac.uns.acs.nais.TicketSalesService.event.TicketPurchaseRollbackEvent;
import rs.ac.uns.acs.nais.TicketSalesService.repository.CustomerRepository;

@Component
public class TicketRollbackListener {

    private final CustomerRepository customerRepository;

    public TicketRollbackListener(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.ROLLBACK_QUEUE)
    public void handleRollback(TicketPurchaseRollbackEvent event) {
        customerRepository.cancelTicket(event.getCustomerId(), event.getSeatId());
    }
}
