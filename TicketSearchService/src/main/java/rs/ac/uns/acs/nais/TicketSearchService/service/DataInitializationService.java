package rs.ac.uns.acs.nais.TicketSearchService.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import rs.ac.uns.acs.nais.TicketSearchService.model.elastic.EventDocument;
import rs.ac.uns.acs.nais.TicketSearchService.model.elastic.TicketSaleDocument;
import rs.ac.uns.acs.nais.TicketSearchService.repository.elastic.EventElasticRepository;
import rs.ac.uns.acs.nais.TicketSearchService.repository.elastic.TicketSaleElasticRepository;

import java.util.*;

@Service
public class DataInitializationService implements ApplicationRunner {

    private static final int TARGET_EVENTS = 50;
    private static final int TARGET_TICKETS = 100;
    private static final String HOME_TEAM = "FK Vojvodina";
    private static final String VENUE = "Stadion Karadjordje";
    private static final String CITY = "Novi Sad";
    private static final String ORGANIZER = "FK Vojvodina";

    private final EventElasticRepository eventRepo;
    private final TicketSaleElasticRepository ticketRepo;
    private final Random random = new Random(42);

    private static final String[] COMPETITIONS = {"SUPER_LIGA", "KUP_SRBIJE", "PRIJATELJSKA", "EVROPSKA_LIGA", "KONFERENCIJSKA_LIGA"};
    private static final String[] STATUSES = {"UPCOMING", "COMPLETED", "ONGOING", "CANCELLED"};
    private static final String[] AWAY_TEAMS = {
        "FK Partizan", "FK Crvena zvezda", "FK Radnicki Nis", "FK Cukaricki",
        "FK Napredak", "FK Javor", "FK Spartak Subotica", "FK Backa",
        "FK Proleter Novi Sad", "FK TSC Backa Topola", "FK Metalac",
        "FK OFK Beograd", "FK Radnik Surdulica", "FK Novi Pazar",
        "FK Vozdovac", "FK Indija", "FK Sloboda Point Sevojno",
        "Real Madrid CF", "Ajax Amsterdam", "Fenerbahce SK",
        "GNK Dinamo Zagreb", "NK Maribor", "HNK Hajduk Split", "HNK Rijeka",
        "FK CSKA Moskva", "Ludogorets Razgrad", "FK Zalgiris Vilnius",
        "Shamrock Rovers FC", "FK Aktobe", "Hibernian FC",
        "FC Zurich", "Servette FC", "FC Basel", "Young Boys",
        "FC Copenhagen", "Brondby IF", "Midtjylland",
        "Galatasaray SK", "Trabzonspor", "Besiktas JK",
        "Sporting CP", "SC Braga", "Vitoria Guimaraes",
        "RSC Anderlecht", "Club Brugge", "KAA Gent",
        "Ferencvaros TC", "MTK Budapest FC", "Paksi FC"
    };
    private static final String[] TICKET_TYPES = {"STANDARD", "VIP", "FAMILY", "GROUP", "STUDENT"};
    private static final String[] PAYMENT_METHODS = {"CREDIT_CARD", "DEBIT_CARD", "CASH", "ONLINE_BANKING", "PAYPAL"};
    private static final String[] ZONE_NAMES = {"VIP Tribina", "Tribina Sever", "Tribina Jug", "Gostujuca Tribina", "Centralni Sektor"};
    private static final String[] SEAT_CATEGORIES = {"VIP", "STANDARD", "ECONOMY"};
    private static final String[] CUSTOMER_NAMES = {
        "Marko Markovic", "Ana Jovic", "Stefan Nikolic", "Milica Petrovic",
        "Nikola Stojanovic", "Jelena Ilic", "Aleksandar Popovic", "Ivana Djordjevic",
        "Vladimir Milosevic", "Natasa Jovanovic", "Dusan Savic", "Maja Lazic",
        "Bojan Petrovic", "Dragana Pavlovic", "Milan Kovacevic", "Tamara Stojanovic",
        "Nemanja Ilic", "Kristina Radovic", "Uros Simic", "Sandra Markovic",
        "Luka Milovanovic", "Jelena Stankovic", "Milos Ristovic", "Ivana Ninkovic",
        "Darko Todorovic", "Vesna Jokic", "Srdjan Bogdanovic", "Maja Miric",
        "Igor Arsenijevic", "Sanja Vukovic", "Petar Lazovic", "Marija Delic",
        "Zoran Pavlovic", "Katarina Simic", "Dejan Antic", "Jovana Ristic",
        "Mladen Jeremic", "Tanja Savic", "Danilo Radovanovic", "Lena Vukovic"
    };

    public DataInitializationService(EventElasticRepository eventRepo, TicketSaleElasticRepository ticketRepo) {
        this.eventRepo = eventRepo;
        this.ticketRepo = ticketRepo;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (eventRepo.count() < TARGET_EVENTS) initEvents();
        if (ticketRepo.count() < TARGET_TICKETS) initTicketSales();
    }

    private void initEvents() {
        List<EventDocument> events = new ArrayList<>();
        int baseYear = 2015;
        for (int i = 0; i < TARGET_EVENTS; i++) {
            String awayTeam = AWAY_TEAMS[i % AWAY_TEAMS.length];
            String competition = COMPETITIONS[i % COMPETITIONS.length];
            int season = baseYear + (i / 40);
            int month = 1 + (i % 10);
            int day = 1 + (i % 28);

            EventDocument doc = new EventDocument();
            doc.setId(UUID.randomUUID().toString());
            doc.setEventId("evt-vojvodina-" + String.format("%04d", i + 1));
            doc.setName(HOME_TEAM + " - " + awayTeam);
            doc.setDescription("Fudbalska utakmica " + competition.replace("_", " ") + ". FK Vojvodina ugostuje " + awayTeam + " na Stadionu Karadjordje u Novom Sadu.");
            doc.setVenue(VENUE);
            doc.setCity(CITY);
            doc.setOrganizer(ORGANIZER);
            doc.setEventType(competition);
            doc.setStatus(STATUSES[i % STATUSES.length]);
            doc.setBasePrice(500.0 + (i % 10) * 200.0);
            doc.setTotalCapacity(15765);
            doc.setAvailableSeats(random.nextInt(15766));
            doc.setDate(String.format("%04d-%02d-%02d", season, month, day));
            events.add(doc);
        }
        eventRepo.saveAll(events);
    }

    private void initTicketSales() {
        List<EventDocument> eventList = new ArrayList<>();
        eventRepo.findAll().forEach(eventList::add);
        if (eventList.isEmpty()) return;

        List<TicketSaleDocument> tickets = new ArrayList<>();
        int customersCount = CUSTOMER_NAMES.length;
        int ticketsPerCustomer = (int) Math.ceil((double) TARGET_TICKETS / customersCount);

        for (int c = 0; c < customersCount && tickets.size() < TARGET_TICKETS; c++) {
            String customerId = String.format("cust-%03d", c + 1);
            String customerName = CUSTOMER_NAMES[c];
            String customerEmail = customerName.toLowerCase().replace(" ", ".") + "@example.com";

            for (int t = 0; t < ticketsPerCustomer && tickets.size() < TARGET_TICKETS; t++) {
                EventDocument event = eventList.get((c * ticketsPerCustomer + t) % eventList.size());
                String zoneName = ZONE_NAMES[random.nextInt(ZONE_NAMES.length)];
                String ticketType = TICKET_TYPES[random.nextInt(TICKET_TYPES.length)];
                double discountPercent = (random.nextInt(4)) * 5.0;
                double multiplier = 0.8 + random.nextInt(15) * 0.1;
                double finalPrice = Math.round(event.getBasePrice() * multiplier * (1.0 - discountPercent / 100.0) * 100.0) / 100.0;
                int year = 2020 + random.nextInt(5);
                int month = 1 + random.nextInt(12);
                int day = 1 + random.nextInt(28);

                TicketSaleDocument ticket = new TicketSaleDocument();
                ticket.setId(UUID.randomUUID().toString());
                ticket.setCustomerId(customerId);
                ticket.setCustomerName(customerName);
                ticket.setCustomerEmail(customerEmail);
                ticket.setEventId(event.getEventId());
                ticket.setEventName(event.getName());
                ticket.setEventDate(event.getDate());
                ticket.setEventType(event.getEventType());
                ticket.setVenue(event.getVenue());
                ticket.setZoneId("zone-" + (random.nextInt(5) + 1));
                ticket.setZoneName(zoneName);
                ticket.setSeatId("seat-" + UUID.randomUUID().toString().substring(0, 8));
                ticket.setSeatCategory(SEAT_CATEGORIES[random.nextInt(SEAT_CATEGORIES.length)]);
                ticket.setPrice(finalPrice);
                ticket.setTicketType(ticketType);
                ticket.setDiscountPercent(discountPercent);
                ticket.setPaymentMethod(PAYMENT_METHODS[random.nextInt(PAYMENT_METHODS.length)]);
                ticket.setPurchaseDate(String.format("%04d-%02d-%02d", year, month, day));
                ticket.setLoyaltyPointsEarned((int) (finalPrice / 100));
                tickets.add(ticket);
            }
        }
        ticketRepo.saveAll(tickets);
    }
}
