package NAIS.PlayerRecommendationService.repository;

import NAIS.PlayerRecommendationService.models.nodes.Report;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepo extends Neo4jRepository<Report, String> {
    @Query("MATCH (r:Report)-[v:VALUED]->(m:Metric) " +
            "WHERE id(r) = $reportId AND id(m) = $metricId " +
            "SET v.score = $newScore " +
            "RETURN count(v)")
    Integer updateMetricScore(String reportId, String metricId, Integer newScore);
}
