package com.example.RecommendedActivityService.repository;

import com.example.RecommendedActivityService.model.Player;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends Neo4jRepository<Player, Long> {
    Player findByPlayerId(Long Id);
    List<Player> findByName(String name);

    List<Player> findBySurname(String surname);

    @Query("MATCH (c:Club)-[r:PARTICIPATED_IN {role: 'HOME'}]->(m:Match) " +
            "MATCH (p:Player)-[:OF_PLAYER]->(mem:Membership)-[perf:PERFORMED_IN]->(m) " +
            "WITH p, SUM(perf.goals) AS totalGoals " +
            "WHERE totalGoals > 0 " +
            "RETURN p ORDER BY totalGoals DESC LIMIT 1")
    Player findTopScorerAtHomeMatches();

    @Query("MATCH (c:Club)-[r:PARTICIPATED_IN]->(m:Match) " +
            "WHERE r.goalsAgainst > r.goalsFor " +
            "MATCH (p:Player)-[:OF_PLAYER]->(mem:Membership)-[perf:PERFORMED_IN]->(m) " +
            "WITH p, SUM(perf.redCards) AS totalRedCards " +
            "WHERE totalRedCards > 0 " +
            "RETURN p ORDER BY totalRedCards DESC LIMIT 1")
    Player findPlayerWithMostRedCardsInLostMatches();
}
