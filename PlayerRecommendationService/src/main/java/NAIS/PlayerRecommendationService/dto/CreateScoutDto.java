package NAIS.PlayerRecommendationService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateScoutDto {
    private String name;
    private String surname;
    private Integer yearsOfExperience;
    private Integer reliabilityRating;
}
