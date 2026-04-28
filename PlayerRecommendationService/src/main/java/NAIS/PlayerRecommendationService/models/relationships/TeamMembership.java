package NAIS.PlayerRecommendationService.models.relationships;

import NAIS.PlayerRecommendationService.models.nodes.Team;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;

import java.time.LocalDate;

@RelationshipProperties
@Getter
@Setter
public class TeamMembership {
    @RelationshipId
    @GeneratedValue
    private Long id;
    private String position;
    private LocalDate startDate;
    private LocalDate endDate;

    @TargetNode
    private Team team;
}
