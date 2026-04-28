package NAIS.PlayerRecommendationService.models.nodes;

import NAIS.PlayerRecommendationService.models.relationships.Valued;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node("Scout")
@Getter
@Setter
public class Scout {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String surname;
    private Integer yearsOfExperience;
    private Integer reliabilityRating;

    @Relationship(value = "CREATED", direction = Relationship.Direction.OUTGOING)
    List<Report> reports;
}
