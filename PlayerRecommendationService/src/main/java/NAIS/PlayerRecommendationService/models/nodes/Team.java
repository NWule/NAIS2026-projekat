package NAIS.PlayerRecommendationService.models.nodes;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Team")
@Getter
@Setter
public class Team {
    @Id
    @GeneratedValue(GeneratedValue.UUIDGenerator.class)
    private String id;
    private String name;
    private String country;
}
