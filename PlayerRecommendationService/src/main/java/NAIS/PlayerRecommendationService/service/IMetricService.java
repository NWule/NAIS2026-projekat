package NAIS.PlayerRecommendationService.service;

import NAIS.PlayerRecommendationService.dto.CreateMetricDto;
import NAIS.PlayerRecommendationService.dto.CreateTeamDto;
import NAIS.PlayerRecommendationService.models.nodes.Metric;

import java.util.List;

public interface IMetricService {

    public List<Metric> getAllMetrics();

    public Metric getMetricById(String id);

    public Metric getMetricByName(String name);

    public Metric saveMetric(CreateMetricDto newMetric);

    public Metric updateMetric(String id, CreateMetricDto updatedMetric);

    public boolean deleteMetric(String id);
}
