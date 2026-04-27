package NAIS.PlayerRecommendationService.models.nodes;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Team")
public class Team {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String country;
}
