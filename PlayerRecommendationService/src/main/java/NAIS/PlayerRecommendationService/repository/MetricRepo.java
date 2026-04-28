package NAIS.PlayerRecommendationService.repository;

import NAIS.PlayerRecommendationService.models.nodes.Metric;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricRepo extends Neo4jRepository<Metric, Long> {
    Metric findByName(String name);
}
