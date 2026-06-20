package com.example.RecommendedActivityService.repository;

import com.example.RecommendedActivityService.model.Match;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends Neo4jRepository<Match, Long> {
    Match getByMatchId(Long id);

    @Query("MATCH (m:Match {matchId: $id}) " +
            "OPTIONAL MATCH (m)-[r:PERFORMANCE]-() " +
            "SET m.isDeleted = true, r.isDeleted = true")
    void softDeleteMatchAndPerformances(@Param("id") Long id);

    @Query("MATCH (m:Match {matchId: $id}) " +
            "OPTIONAL MATCH (m)-[r:PERFORMANCE]-() " +
            "SET m.isDeleted = false, r.isDeleted = false")
    void rollbackSoftDelete(@Param("id") Long id);

    @Query("MATCH (m:Match {matchId: $id}) " +
            "WHERE m.isDeleted = false OR m.isDeleted IS NULL " +
            "RETURN m")
    Match getActiveMatchById(@Param("id") Long id);
}
