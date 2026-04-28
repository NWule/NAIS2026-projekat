package NAIS.PlayerRecommendationService.controller;

import NAIS.PlayerRecommendationService.dto.CreateMetricDto;
import NAIS.PlayerRecommendationService.models.nodes.Metric;
import NAIS.PlayerRecommendationService.service.impl.MetricService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metrics")
public class MetricController {

    private final MetricService metricService;

    public MetricController(MetricService metricService) {
        this.metricService = metricService;
    }

    @GetMapping("")
    public ResponseEntity<List<Metric>> getAllMetrics() {
        return ResponseEntity.ok(metricService.getAllMetrics());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Metric> getMetricById(@PathVariable Long id) {
        Metric metric = metricService.getMetricById(id);
        if (metric != null) {
            return ResponseEntity.ok(metric);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("")
    public ResponseEntity<Metric> saveMetric(@RequestBody CreateMetricDto newMetric) {
        return ResponseEntity.ok(metricService.saveMetric(newMetric));
    }

    @PutMapping("")
    public ResponseEntity<Metric> updateMetric(@RequestParam Long id, @RequestBody CreateMetricDto updatedMetric) {
        Metric metric = metricService.updateMetric(id, updatedMetric);
        if (metric != null) {
            return ResponseEntity.ok(metric);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteMetric(@RequestParam Long id) {
        if (metricService.deleteMetric(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
