package NAIS.PlayerRecommendationService.repository;

import NAIS.PlayerRecommendationService.models.nodes.Player;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlayerRepo extends Neo4jRepository<Player, Long> {
    Player findByNameAndSurname(String name, String surname);

    @Query("MATCH (p:Player)-[r:PLAYS_FOR]->(t:Team) " +
            "WHERE id(p) = $playerId AND id(t) = $teamId AND r.endDate IS NULL " +
            "SET r.endDate = $endDate " +
            "RETURN p")
    void endTeamMembership(Long playerId, Long teamId, LocalDate endDate);

    @Query("MATCH (p:Player)<-[:IS_ABOUT]-(r:Report)-[v:VALUED]->(m:Metric) " +
            "WHERE id(m) IN $metricIds " +
            "WITH p, sum(v.score) AS totalScore " +
            "ORDER BY totalScore DESC " +
            "LIMIT 1 " +
            "RETURN p")
    Player findPlayerWithBestScoreForMetrics(@Param("metricIds") List<Long> metricIds);

    @Query("MATCH (target:Player) WHERE id(target) = $playerId " +
            "MATCH (target)<-[:IS_ABOUT]-(:Report)-[v1:VALUED]->(m:Metric) " +
            "WHERE v1.score > 8.0 " +
            "MATCH (m)<-[v2:VALUED]-(:Report)-[:IS_ABOUT]->(similarPlayer:Player) " +
            "WHERE v2.score > 8.0 AND similarPlayer <> target " +
            "WITH similarPlayer, COUNT(m) AS sharedStrongMetrics " +
            "ORDER BY sharedStrongMetrics DESC " +
            "RETURN similarPlayer " +
            "LIMIT 5")
    List<Player> findSimilarPlayers(@Param("playerId") Long playerId);

    @Query("MATCH (s:Scout)-[:CREATED]->(r:Report)-[:IS_ABOUT]->(p:Player) " +
            "WHERE s.reliabilityScore >= $minReliability " +
            "  AND p.dateOfBirth >= $cutoffDate " +
            "MATCH (r)-[v:VALUED]->(:Metric) " +
            "WITH p, count(DISTINCT r) AS reportCount, avg(v.score) AS averageScore " +
            "WHERE averageScore >= $minAvgScore " +
            "ORDER BY averageScore DESC " +
            "LIMIT 10 " +
            "RETURN p")
    List<Player> findEliteProspects(
            @Param("cutoffDate") LocalDate cutoffDate,
            @Param("minReliability") Integer minReliability,
            @Param("minAvgScore") Double minAvgScore
    );
}
