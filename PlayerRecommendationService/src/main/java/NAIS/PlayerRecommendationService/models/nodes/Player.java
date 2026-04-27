package NAIS.PlayerRecommendationService.models.nodes;

import NAIS.PlayerRecommendationService.models.relationships.TeamMembership;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
import java.util.List;

@Node("Player")
@Getter
@Setter
public class Player {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String nationality;

    @Relationship(value = "PLAYS_FOR", direction = Relationship.Direction.OUTGOING)
    private List<TeamMembership> teams;
}
