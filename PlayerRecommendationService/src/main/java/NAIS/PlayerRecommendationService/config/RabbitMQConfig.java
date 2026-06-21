package NAIS.PlayerRecommendationService.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "scouting.report.exchange";

    public static final String REPORT_CREATED_QUEUE = "report.created.queue";
    public static final String REPORT_CREATED_CONFIRMATION_QUEUE = "report.created.confirmation.queue";

    public static final String REPORT_CREATED_FAILED_KEY =   "report.created.failed";
    public static final String REPORT_CREATED_KEY =   "report.created";
    public static final String REPORT_CREATED_PROCESSED_KEY =   "report.created.processed";

    // rabbitMQ konfiguracija

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    // deklaracija topic exchange-a

    @Bean
    public TopicExchange reportExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // deklaracija queue-ova

    @Bean
    public Queue reportCreatedQueue() {
        return QueueBuilder.durable(REPORT_CREATED_QUEUE).build();
    }

    @Bean
    public Queue reportCreatedConfirmationQueue() {
        return QueueBuilder.durable(REPORT_CREATED_CONFIRMATION_QUEUE).build();
    }

    // deklaracija binding-ova

    @Bean
    public Binding reportCreatedBinding() {
        return BindingBuilder.bind(reportCreatedQueue())
                .to(reportExchange())
                .with(REPORT_CREATED_KEY);
    }

    @Bean
    public Binding reportCreatedConfirmationBinding() {
        return BindingBuilder.bind(reportCreatedConfirmationQueue())
                .to(reportExchange())
                .with(REPORT_CREATED_PROCESSED_KEY);
    }

    @Bean
    public Binding reportCreatedFailedBinding() {
        return BindingBuilder.bind(reportCreatedConfirmationQueue())
                .to(reportExchange())
                .with(REPORT_CREATED_FAILED_KEY);
    }
}
