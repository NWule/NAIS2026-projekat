package rs.ac.uns.acs.nais.TicketSearchService.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "ticket-saga-exchange";
    public static final String PURCHASED_QUEUE = "ticket-purchased-queue";
    public static final String CANCELLED_QUEUE = "ticket-cancelled-queue";
    public static final String ROLLBACK_QUEUE = "ticket-rollback-queue";

    @Bean
    public TopicExchange sagaExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue purchasedQueue() {
        return new Queue(PURCHASED_QUEUE, true);
    }

    @Bean
    public Queue cancelledQueue() {
        return new Queue(CANCELLED_QUEUE, true);
    }

    @Bean
    public Queue rollbackQueue() {
        return new Queue(ROLLBACK_QUEUE, true);
    }

    @Bean
    public Binding purchasedBinding() {
        return BindingBuilder.bind(purchasedQueue()).to(sagaExchange()).with("ticket.purchased");
    }

    @Bean
    public Binding cancelledBinding() {
        return BindingBuilder.bind(cancelledQueue()).to(sagaExchange()).with("ticket.cancelled");
    }

    @Bean
    public Binding rollbackBinding() {
        return BindingBuilder.bind(rollbackQueue()).to(sagaExchange()).with("ticket.rollback");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(DefaultJackson2JavaTypeMapper.TypePrecedence.INFERRED);
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
