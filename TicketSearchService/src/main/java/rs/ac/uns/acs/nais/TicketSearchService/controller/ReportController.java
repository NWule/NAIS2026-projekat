package rs.ac.uns.acs.nais.TicketSearchService.controller;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.acs.nais.TicketSearchService.service.ReportService;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateReport(
            @RequestParam(defaultValue = "500") Double minPrice,
            @RequestParam(defaultValue = "10000") Double maxPrice) {

        byte[] pdf = reportService.generateReport(minPrice, maxPrice);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment().filename("vojvodina-izvestaj.pdf").build());

        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}
