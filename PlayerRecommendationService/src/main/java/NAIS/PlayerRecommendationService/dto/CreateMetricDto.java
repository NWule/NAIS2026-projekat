package NAIS.PlayerRecommendationService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMetricDto {
    private String name;
    private String description;
    private String category;
}
