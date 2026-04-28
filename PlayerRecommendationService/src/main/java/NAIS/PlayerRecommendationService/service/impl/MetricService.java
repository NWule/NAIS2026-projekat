package NAIS.PlayerRecommendationService.service.impl;

import NAIS.PlayerRecommendationService.dto.CreateMetricDto;
import NAIS.PlayerRecommendationService.models.nodes.Metric;
import NAIS.PlayerRecommendationService.repository.MetricRepo;
import NAIS.PlayerRecommendationService.service.IMetricService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetricService implements IMetricService {
    private final MetricRepo metricRepo;

    public MetricService(MetricRepo metricRepo) {
        this.metricRepo = metricRepo;
    }

    @Override
    public List<Metric> getAllMetrics() {
        return metricRepo.findAll();
    }

    @Override
    public Metric getMetricById(Long id) {
        return metricRepo.findById(id).orElse(null);
    }

    @Override
    public Metric getMetricByName(String name) {
        return metricRepo.findByName(name);
    }

    @Override
    public Metric saveMetric(CreateMetricDto metricDto) {
        Metric newMetric = new Metric();
        newMetric.setName(metricDto.getName());
        newMetric.setDescription(metricDto.getDescription());
        newMetric.setCategory(metricDto.getCategory());
        return metricRepo.save(newMetric);
    }

    @Override
    public Metric updateMetric(Long id, CreateMetricDto metricDto) {
        Metric metricToUpdate = metricRepo.findById(id).orElse(null);
        if (metricToUpdate != null) {
            metricToUpdate.setName(metricDto.getName());
            metricToUpdate.setDescription(metricDto.getDescription());
            metricToUpdate.setCategory(metricDto.getCategory());
            return metricRepo.save(metricToUpdate);
        }
        return null;
    }

    @Override
    public boolean deleteMetric(Long id) {
        if (metricRepo.findById(id).isEmpty()) {
            return false;
        }
        metricRepo.deleteById(id);
        return true;
    }
}
