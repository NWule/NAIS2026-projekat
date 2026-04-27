package NAIS.PlayerRecommendationService.models.nodes;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Metric")
public class Metric {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private String category;
}
