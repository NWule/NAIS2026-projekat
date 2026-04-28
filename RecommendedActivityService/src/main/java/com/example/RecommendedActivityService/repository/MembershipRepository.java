package com.example.RecommendedActivityService.repository;

import com.example.RecommendedActivityService.model.Membership;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipRepository extends Neo4jRepository<Membership, Long> {

    Membership getById(Long id);

    @Query("""
        MATCH (p:Player)-[:OF_PLAYER]->(m:Membership)
        WHERE p.playerId = $playerId
        RETURN m
    """)
    List<Membership> findByPlayerId(Long playerId);

    @Query("""
        MATCH (m:Membership)-[:OF_CLUB]->(c:Club)
        WHERE c.clubId = $clubId
        RETURN m
    """)
    List<Membership> findByClubId(Long clubId);

    @Query("""
        MATCH (p:Player)-[:OF_PLAYER]->(m:Membership)
        WHERE p.playerId = $playerId AND m.toDate IS NULL
        RETURN m
        LIMIT 1
    """)
    Membership findCurrentByPlayerId(Long playerId);

    @Query("""
        MATCH (m:Membership)
        WHERE id(m) = $id
        DETACH DELETE m
    """)
    void deleteById(Long id);

    @Query("""
        MATCH (m:Membership), (ma:Match)
        WHERE id(m) = $membershipId AND ma.matchId = $matchId
        OPTIONAL MATCH (m)-[existing:PERFORMED_IN]->(ma)
        WITH m, ma, existing
        WHERE existing IS NULL
        CREATE (m)-[r:PERFORMED_IN {
            goals: $goals,
            assists: $assists,
            minutesPlayed: $minutes,
            yellowCards: $yellow,
            redCards: $red
        }]->(ma)
    """)
    void createPerformance(Long membershipId, Long matchId,
                           int goals, int assists,
                           int minutes, int yellow, int red);

    @Query("""
        MATCH (m:Membership)-[r:PERFORMED_IN]->(ma:Match)
        WHERE id(m) = $membershipId AND ma.matchId = $matchId
        SET r.goals = $goals,
            r.assists = $assists,
            r.minutesPlayed = $minutes,
            r.yellowCards = $yellow,
            r.redCards = $red
    """)
    void updatePerformance(Long membershipId, Long matchId,
                           int goals, int assists,
                           int minutes, int yellow, int red);

    @Query("""
        MATCH (m:Membership)-[r:PERFORMED_IN]->(ma:Match)
        WHERE id(m) = $membershipId AND ma.matchId = $matchId
        RETURN count(r) > 0
    """)
    boolean existsPerformance(Long membershipId, Long matchId);

    @Query("""
        MATCH (m:Membership)-[r:PERFORMED_IN]->(ma:Match)
        WHERE id(m) = $membershipId AND ma.matchId = $matchId
        DELETE r
    """)
    void deletePerformance(Long membershipId, Long matchId);
}