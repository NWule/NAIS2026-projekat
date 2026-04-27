package NAIS.PlayerRecommendationService.models.relationships;

import NAIS.PlayerRecommendationService.models.nodes.Report;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.time.LocalDate;

@RelationshipProperties
@Getter
@Setter
public class Valued {
    @RelationshipId
    @GeneratedValue
    private Long id;
    private LocalDate creationDate;

    @TargetNode
    private Report report;
}
