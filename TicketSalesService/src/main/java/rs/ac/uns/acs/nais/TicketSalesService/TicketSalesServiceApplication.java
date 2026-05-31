package rs.ac.uns.acs.nais.TicketSalesService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableNeo4jRepositories(basePackages = "rs.ac.uns.acs.nais.TicketSalesService.repository")
@EnableElasticsearchRepositories(basePackages = "rs.ac.uns.acs.nais.TicketSalesService.repository.elastic")
public class TicketSalesServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TicketSalesServiceApplication.class, args);
    }
}
