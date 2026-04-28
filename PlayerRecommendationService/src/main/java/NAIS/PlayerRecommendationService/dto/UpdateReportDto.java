package NAIS.PlayerRecommendationService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateReportDto {
    private String text;
    private Integer score;
    private String game;
}
