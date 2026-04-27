package com.example.RecommendedActivityService.repository;

import com.example.RecommendedActivityService.model.Match;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends Neo4jRepository<Match, Integer> {
    Match getById(Integer id);

}
