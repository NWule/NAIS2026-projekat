package rs.ac.uns.acs.nais.TicketSearchService.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.acs.nais.TicketSearchService.model.elastic.TicketSaleDocument;
import rs.ac.uns.acs.nais.TicketSearchService.repository.elastic.TicketSaleElasticRepository;
import rs.ac.uns.acs.nais.TicketSearchService.repository.elastic.TicketSaleElasticRepositoryCustom;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final TicketSaleElasticRepository ticketSaleRepository;
    private final TicketSaleElasticRepositoryCustom customRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ticket-sales-service.url:http://localhost:9070}")
    private String ticketSalesUrl;

    private static final java.awt.Color HEADER_BG  = new java.awt.Color(30, 60, 114);
    private static final java.awt.Color SECTION_BG = new java.awt.Color(52, 100, 171);
    private static final java.awt.Color ROW_ALT    = new java.awt.Color(235, 241, 251);
    private static final java.awt.Color WHITE       = java.awt.Color.WHITE;

    public ReportService(TicketSaleElasticRepository ticketSaleRepository,
                         TicketSaleElasticRepositoryCustom customRepository) {
        this.ticketSaleRepository = ticketSaleRepository;
        this.customRepository = customRepository;
    }

    public byte[] generateReport(Double minPrice, Double maxPrice) {
        List<TicketSaleDocument> tickets = ticketSaleRepository.findByPriceBetween(minPrice, maxPrice);
        List<Map<String, Object>> topCustomers = fetchTopCustomers();
        List<Map<String, Object>> topEvents = customRepository.getTopEventsByRevenue(10, null);
        List<Map<String, Object>> analytics = customRepository.getSalesAnalyticsByEventTypeAndPaymentMethod("2015-01-01", "2040-12-31");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4.rotate(), 30, 30, 40, 30);
        try {
            PdfWriter.getInstance(doc, baos);
            doc.open();

            addReportHeader(doc, minPrice, maxPrice);
            addSection1(doc, tickets, minPrice, maxPrice);
            doc.newPage();
            addSection2(doc, topCustomers);
            doc.newPage();
            addSection3(doc, analytics);
            doc.newPage();
            addChartSection(doc, topEvents);

            doc.close();
        } catch (Exception e) {
            throw new RuntimeException("Greska pri generisanju PDF izvestaja", e);
        }
        return baos.toByteArray();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<Map<String, Object>> fetchTopCustomers() {
        try {
            Map<String, Object>[] arr = restTemplate.getForObject(
                    ticketSalesUrl + "/customers/analytics/top", Map[].class);
            return arr != null ? List.of(arr) : new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private void addReportHeader(Document doc, Double minPrice, Double maxPrice) throws DocumentException {
        BaseFont bf = safeFont();
        Font titleFont  = new Font(bf, 20, Font.BOLD, WHITE);
        Font subtitleFont = new Font(bf, 10, Font.NORMAL, WHITE);

        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);
        PdfPCell titleCell = new PdfPCell(new Phrase("FK Vojvodina - Izvestaj Prodaje Karata", titleFont));
        titleCell.setBackgroundColor(HEADER_BG);
        titleCell.setPadding(14);
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleCell.setBorder(Rectangle.NO_BORDER);
        header.addCell(titleCell);

        PdfPCell subCell = new PdfPCell(new Phrase(
            "Generisano: " + LocalDate.now() + "  |  Cenovni opseg Sekcije 1: " +
            minPrice.intValue() + " - " + maxPrice.intValue() + " RSD", subtitleFont));
        subCell.setBackgroundColor(SECTION_BG);
        subCell.setPadding(6);
        subCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        subCell.setBorder(Rectangle.NO_BORDER);
        header.addCell(subCell);

        doc.add(header);
        doc.add(Chunk.NEWLINE);
    }

    private void addSection1(Document doc, List<TicketSaleDocument> tickets,
                              Double minPrice, Double maxPrice) throws DocumentException {
        addSectionTitle(doc, "1. PROSTA SEKCIJA: Prodane Karte u Cenovnom Opsegu [Elasticsearch]",
            "Filtriranje po ceni: " + minPrice.intValue() + " - " + maxPrice.intValue() +
            " RSD  |  Ukupno pronaseno: " + tickets.size() + " karata");

        String[] headers = {"Kupac", "Event", "Venue", "Zona", "Tip Karte", "Cena (RSD)", "Placanje", "Datum"};
        float[] widths = {14f, 18f, 12f, 11f, 9f, 10f, 10f, 10f};
        PdfPTable table = createTable(headers, widths);

        BaseFont bf = safeFont();
        Font dataFont = new Font(bf, 7, Font.NORMAL);
        boolean alt = false;
        for (TicketSaleDocument t : tickets) {
            java.awt.Color bg = alt ? ROW_ALT : WHITE;
            addRow(table, dataFont, bg,
                nvl(t.getCustomerName()),
                nvl(t.getEventName()),
                nvl(t.getVenue()),
                nvl(t.getZoneName()),
                nvl(t.getTicketType()),
                t.getPrice() != null ? String.format("%.2f", t.getPrice()) : "-",
                nvl(t.getPaymentMethod()),
                nvl(t.getPurchaseDate()));
            alt = !alt;
        }
        doc.add(table);
    }

    private void addSection2(Document doc, List<Map<String, Object>> customers) throws DocumentException {
        addSectionTitle(doc, "2. PROSTA SEKCIJA: Top 10 Kupaca po Ukupnoj Potrosnji [Neo4j]",
            "Podaci pribavljeni iz grafovske baze podataka Neo4j putem REST poziva na TicketSalesService");

        String[] headers = {"Ime", "Prezime", "Email", "Loyalty Poeni", "Br. Kupovina", "Ukupna Potrosnja (RSD)"};
        float[] widths = {12f, 12f, 25f, 13f, 13f, 18f};
        PdfPTable table = createTable(headers, widths);

        BaseFont bf = safeFont();
        Font dataFont = new Font(bf, 8, Font.NORMAL);
        boolean alt = false;
        for (Map<String, Object> c : customers) {
            java.awt.Color bg = alt ? ROW_ALT : WHITE;
            addRow(table, dataFont, bg,
                nvl(c.get("name")),
                nvl(c.get("surname")),
                nvl(c.get("email")),
                nvl(c.get("loyaltyPoints")),
                nvl(c.get("totalPurchases")),
                c.get("totalSpent") != null ? String.format("%.2f", ((Number) c.get("totalSpent")).doubleValue()) : "-");
            alt = !alt;
        }
        doc.add(table);
    }

    private void addSection3(Document doc, List<Map<String, Object>> analytics) throws DocumentException {
        addSectionTitle(doc, "3. SLOZENA SEKCIJA: Prihodi po Tipu Dogadjaja i Nacinu Placanja [Elasticsearch]",
            "Visestruka (nested) agregacija: eventType -> paymentMethod -> SUM(price), COUNT, AVG(discount)");

        String[] headers = {"Tip Dogadjaja", "Nacin Placanja", "Ukupni Prihod Tipa (RSD)",
                            "Prihod Placanja (RSD)", "Broj Karata", "Avg. Popust %"};
        float[] widths = {14f, 14f, 20f, 20f, 12f, 12f};
        PdfPTable table = createTable(headers, widths);

        BaseFont bf = safeFont();
        Font dataFont = new Font(bf, 8, Font.NORMAL);
        boolean alt = false;
        for (Map<String, Object> row : analytics) {
            java.awt.Color bg = alt ? ROW_ALT : WHITE;
            addRow(table, dataFont, bg,
                nvl(row.get("eventType")),
                nvl(row.get("paymentMethod")),
                formatDouble(row.get("totalEventRevenue")),
                formatDouble(row.get("paymentRevenue")),
                formatLong(row.get("ticketCount")),
                formatDouble(row.get("avgDiscount")));
            alt = !alt;
        }
        doc.add(table);
    }

    private void addChartSection(Document doc, List<Map<String, Object>> topEvents) throws Exception {
        addSectionTitle(doc, "4. GRAFIKON: Top 5 Dogadjaja po Ukupnom Prihodu [Elasticsearch]",
            "Prikaz pet najuspesnijih dogadjaja po ostvarenoj zaradi od prodaje karata");

        List<Map<String, Object>> top5 = topEvents.stream().limit(5).collect(Collectors.toList());

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map<String, Object> e : top5) {
            String name = nvl(e.get("eventName"));
            if (name.length() > 25) name = name.substring(0, 25) + "...";
            double revenue = e.get("totalRevenue") != null ? ((Number) e.get("totalRevenue")).doubleValue() : 0;
            dataset.addValue(revenue, "Prihod (RSD)", name);
        }

        JFreeChart chart = ChartFactory.createBarChart(
            "Top 5 Dogadjaja po Prihodu",
            "Dogadjaj",
            "Prihod (RSD)",
            dataset,
            PlotOrientation.VERTICAL,
            false, true, false
        );

        chart.setBackgroundPaint(java.awt.Color.WHITE);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new java.awt.Color(245, 248, 255));
        plot.setRangeGridlinePaint(java.awt.Color.LIGHT_GRAY);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, HEADER_BG);
        renderer.setDrawBarOutline(false);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setMaximumCategoryLabelLines(3);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRangeIncludesZero(true);

        BufferedImage chartImage = chart.createBufferedImage(750, 350);
        ByteArrayOutputStream chartBaos = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "PNG", chartBaos);
        Image pdfImage = Image.getInstance(chartBaos.toByteArray());
        pdfImage.setAlignment(Element.ALIGN_CENTER);
        pdfImage.scaleToFit(750, 350);
        doc.add(pdfImage);

        doc.add(Chunk.NEWLINE);
        addTopEventsTable(doc, topEvents);
    }

    private void addTopEventsTable(Document doc, List<Map<String, Object>> topEvents) throws DocumentException {
        BaseFont bf = safeFont();
        Font captionFont = new Font(bf, 8, Font.ITALIC, new java.awt.Color(80, 80, 80));
        doc.add(new Paragraph("Tabela: Kompletna lista top 10 dogadjaja po prihodu", captionFont));
        doc.add(Chunk.NEWLINE);

        String[] headers = {"#", "Naziv Dogadjaja", "Ukupni Prihod (RSD)", "Broj Karata", "Avg. Cena (RSD)", "Avg. Popust %"};
        float[] widths = {4f, 35f, 20f, 13f, 15f, 13f};
        PdfPTable table = createTable(headers, widths);

        Font dataFont = new Font(bf, 8, Font.NORMAL);
        boolean alt = false;
        int rank = 1;
        for (Map<String, Object> e : topEvents) {
            java.awt.Color bg = alt ? ROW_ALT : WHITE;
            addRow(table, dataFont, bg,
                String.valueOf(rank++),
                nvl(e.get("eventName")),
                formatDouble(e.get("totalRevenue")),
                formatLong(e.get("ticketCount")),
                formatDouble(e.get("avgPrice")),
                formatDouble(e.get("avgDiscount")));
            alt = !alt;
        }
        doc.add(table);
    }

    private void addSectionTitle(Document doc, String title, String subtitle) throws DocumentException {
        BaseFont bf = safeFont();
        PdfPTable titleBar = new PdfPTable(1);
        titleBar.setWidthPercentage(100);
        titleBar.setSpacingBefore(10);

        PdfPCell titleCell = new PdfPCell(new Phrase(title, new Font(bf, 10, Font.BOLD, WHITE)));
        titleCell.setBackgroundColor(SECTION_BG);
        titleCell.setPadding(7);
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleBar.addCell(titleCell);

        if (subtitle != null && !subtitle.isBlank()) {
            PdfPCell subCell = new PdfPCell(new Phrase(subtitle, new Font(bf, 8, Font.ITALIC, new java.awt.Color(50, 50, 50))));
            subCell.setBackgroundColor(new java.awt.Color(220, 230, 245));
            subCell.setPadding(5);
            subCell.setBorder(Rectangle.NO_BORDER);
            titleBar.addCell(subCell);
        }
        doc.add(titleBar);
        doc.add(Chunk.NEWLINE);
    }

    private PdfPTable createTable(String[] headers, float[] widths) throws DocumentException {
        BaseFont bf = safeFont();
        PdfPTable table = new PdfPTable(headers.length);
        table.setWidthPercentage(100);
        table.setWidths(widths);

        Font headerFont = new Font(bf, 8, Font.BOLD, WHITE);
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(HEADER_BG);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderColor(new java.awt.Color(200, 210, 230));
            table.addCell(cell);
        }
        return table;
    }

    private void addRow(PdfPTable table, Font font, java.awt.Color bg, String... values) {
        for (String v : values) {
            PdfPCell cell = new PdfPCell(new Phrase(v, font));
            cell.setBackgroundColor(bg);
            cell.setPadding(4);
            cell.setBorderColor(new java.awt.Color(200, 210, 230));
            table.addCell(cell);
        }
    }

    private BaseFont safeFont() {
        try {
            return BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String nvl(Object o) {
        return o != null ? o.toString() : "-";
    }

    private String formatDouble(Object o) {
        if (o == null) return "-";
        return String.format("%.2f", ((Number) o).doubleValue());
    }

    private String formatLong(Object o) {
        if (o == null) return "-";
        return String.valueOf(((Number) o).longValue());
    }
}
