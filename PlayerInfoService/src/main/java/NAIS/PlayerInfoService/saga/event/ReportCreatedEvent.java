package NAIS.PlayerRecommendationService.saga.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ReportCreatedEvent {
    private String psychologicalProfile;
    private String tacticalNotes;
    private String weaknesses;
    private List<String> tags;
    private Integer overallRating;
    private String scoutId;
    private String playerId;
}
