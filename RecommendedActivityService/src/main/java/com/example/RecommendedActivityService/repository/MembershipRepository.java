package com.example.RecommendedActivityService.repository;

import com.example.RecommendedActivityService.model.Membership;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipRepository extends Neo4jRepository<Membership, Integer> {

    Membership getById(Integer id);

    @Query("""
        MATCH (p:Player)-[:OF_PLAYER]->(m:Membership)
        WHERE p.playerId = $playerId
        RETURN m
    """)
    List<Membership> findByPlayerId(Integer playerId);

    @Query("""
        MATCH (m:Membership)-[:OF_CLUB]->(c:Club)
        WHERE c.clubId = $clubId
        RETURN m
    """)
    List<Membership> findByClubId(Integer clubId);

    @Query("""
        MATCH (p:Player)-[:OF_PLAYER]->(m:Membership)
        WHERE p.playerId = $playerId AND m.toDate IS NULL
        RETURN m
        LIMIT 1
    """)
    Membership findCurrentByPlayerId(Integer playerId);

    @Query("""
        MATCH (m:Membership)
        WHERE id(m) = $id
        DETACH DELETE m
    """)
    void deleteById(Integer id);

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
    void createPerformance(Integer membershipId, Integer matchId,
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
    void updatePerformance(Integer membershipId, Integer matchId,
                           int goals, int assists,
                           int minutes, int yellow, int red);

    @Query("""
        MATCH (m:Membership)-[r:PERFORMED_IN]->(ma:Match)
        WHERE id(m) = $membershipId AND ma.matchId = $matchId
        RETURN count(r) > 0
    """)
    boolean existsPerformance(Integer membershipId, Integer matchId);

    @Query("""
        MATCH (m:Membership)-[r:PERFORMED_IN]->(ma:Match)
        WHERE id(m) = $membershipId AND ma.matchId = $matchId
        DELETE r
    """)
    void deletePerformance(Integer membershipId, Integer matchId);
}