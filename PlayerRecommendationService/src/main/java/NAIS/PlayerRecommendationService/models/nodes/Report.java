package NAIS.PlayerRecommendationService.models.nodes;

import NAIS.PlayerRecommendationService.models.relationships.Valued;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node("Report")
@Getter
@Setter
public class Report {
    @Id
    @GeneratedValue
    private Long id;
    private String text;
    private Integer score;
    private String game;

    @Relationship(value = "IS_ABOUT", direction = Relationship.Direction.OUTGOING)
    private Player player;

    @Relationship(value = "VALUED", direction = Relationship.Direction.OUTGOING)
    private List<Valued> metrics;
}
