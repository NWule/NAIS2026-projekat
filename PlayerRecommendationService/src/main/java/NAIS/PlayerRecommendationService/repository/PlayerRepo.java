package NAIS.PlayerRecommendationService.repository;

import NAIS.PlayerRecommendationService.models.nodes.Player;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepo extends Neo4jRepository<Player, Long> {
    Player findByNameAndSurname(String name, String surname);
}
