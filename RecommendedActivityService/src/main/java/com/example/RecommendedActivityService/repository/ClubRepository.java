package com.example.RecommendedActivityService.repository;

import com.example.RecommendedActivityService.model.Club;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.RecommendedActivityService.model.Player;

@Repository
public interface ClubRepository extends Neo4jRepository<Club, Integer> {
    Club getById(Integer Id);

    Club findByName(String name);

    @Query("""
        MATCH (c:Club), (m:Match)
        WHERE c.clubId = $clubId AND m.matchId = $matchId
        CREATE (c)-[r:PARTICIPATED_IN]->(m)
        SET r.role = $role,
                       r.goalsFor = 0,
                       r.goalsAgainst = 0,
                       r.possession = 0,
                       r.shots = 0
        """)
    void createParticipation(Integer clubId, Integer matchId, String role);

    @Query("""
        MATCH (c:Club)-[r:PARTICIPATED_IN]->(m:Match)
        WHERE c.clubId = $clubId AND m.matchId = $matchId
        SET r.role = $role,
                       r.goalsFor = $goalsFor,
                       r.goalsAgainst = $goalsAgainst,
                       r.possession = $possession,
                       r.shots = $shots
    """)
    void updateParticipation(Integer clubId, Integer matchId, String role, int goalsFor,
                             int goalsAgainst, int possession, int shots);

    @Query("""
        MATCH (c:Club {clubId: $clubId})-[r:PARTICIPATED_IN]->(m:Match {matchId: $matchId})
        RETURN count(r) > 0
        """)
     boolean existsParticipation(Integer clubId, Integer matchId);

    @Query("""
        MATCH (c:Club {clubId: $clubId})-[r:PARTICIPATED_IN]->(m:Match {matchId: $matchId})
        DELETE r
        """)
    void deleteParticipation(Integer clubId, Integer matchId);

    @Query("MATCH (c:Club)-[r:PARTICIPATED_IN]->(m:Match)<-[:OF_MATCH]-(mem:Membership)-[:OF_PLAYER]->(p:Player) " +
                    "WITH c, p, r.possession AS possession,  SUM(CASE WHEN mem.goals > 0 THEN 1 ELSE 0 END) AS scoredFlag " +
                    "WITH c, COUNT(DISTINCT p) AS distinctScorers, AVG(possession) AS avgPossession " +
                    "WHERE distinctScorers > 1 " +
                    "RETURN c " +
                    "ORDER BY distinctScorers DESC, avgPossession ASC " +
                    "LIMIT 1")
    Club findBestLowPossessionDiverseScoringClub();
}