package NAIS.PlayerRecommendationService.models.nodes;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Metric")
@Getter
@Setter
public class Metric {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private String category;
}
