package NAIS.PlayerRecommendationService.models.relationships;

import NAIS.PlayerRecommendationService.models.nodes.Metric;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
public class Scored {
    @Id
    @GeneratedValue
    private Long id;
    private Float score;

    @TargetNode
    private Metric metric;
}
