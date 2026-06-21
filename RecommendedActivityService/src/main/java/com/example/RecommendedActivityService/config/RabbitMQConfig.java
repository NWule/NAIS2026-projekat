package com.example.RecommendedActivityService.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CHOREOGRAPHY_EXCHANGE              = "saga.choreography.exchange";

    public static final String MATCH_DELETED_QUEUE                = "match.deleted.queue";
    public static final String MATCH_DELETED_KEY                  = "match.deleted";

    public static final String MATCH_EVENTS_DELETED_QUEUE         = "match.events.deleted.queue";
    public static final String MATCH_EVENTS_DELETED_KEY           = "match.events.deleted";

    public static final String MATCH_EVENTS_DELETION_FAILED_QUEUE = "match.events.deletion.failed.queue";
    public static final String MATCH_EVENTS_DELETION_FAILED_KEY   = "match.events.deletion.failed";

    public static final String MATCH_COMPENSATED_QUEUE            = "match.compensated.queue";
    public static final String MATCH_COMPENSATED_KEY              = "match.compensated";

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }

    @Bean
    public TopicExchange choreographyExchange() {
        return new TopicExchange(CHOREOGRAPHY_EXCHANGE);
    }

    @Bean public Queue matchDeletedQueue()                { return QueueBuilder.durable(MATCH_DELETED_QUEUE).build(); }
    @Bean public Queue matchEventsDeletedQueue()          { return QueueBuilder.durable(MATCH_EVENTS_DELETED_QUEUE).build(); }
    @Bean public Queue matchEventsDeletionFailedQueue()   { return QueueBuilder.durable(MATCH_EVENTS_DELETION_FAILED_QUEUE).build(); }
    @Bean public Queue matchCompensatedQueue()            { return QueueBuilder.durable(MATCH_COMPENSATED_QUEUE).build(); }

    @Bean public Binding matchDeletedBinding()                { return BindingBuilder.bind(matchDeletedQueue()).to(choreographyExchange()).with(MATCH_DELETED_KEY); }
    @Bean public Binding matchEventsDeletedBinding()          { return BindingBuilder.bind(matchEventsDeletedQueue()).to(choreographyExchange()).with(MATCH_EVENTS_DELETED_KEY); }
    @Bean public Binding matchEventsDeletionFailedBinding()   { return BindingBuilder.bind(matchEventsDeletionFailedQueue()).to(choreographyExchange()).with(MATCH_EVENTS_DELETION_FAILED_KEY); }
    @Bean public Binding matchCompensatedBinding()            { return BindingBuilder.bind(matchCompensatedQueue()).to(choreographyExchange()).with(MATCH_COMPENSATED_KEY); }
}
