package rs.ac.uns.acs.nais.TicketSalesService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TicketSalesServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TicketSalesServiceApplication.class, args);
    }
}
