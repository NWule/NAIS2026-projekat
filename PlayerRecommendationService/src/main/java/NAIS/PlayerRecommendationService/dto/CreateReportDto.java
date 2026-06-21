package NAIS.PlayerRecommendationService.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
public class CreateReportDto {
    private String text;
    private Integer score;
    private String game;
    private String scoutId;
    private String playerId;
    private Map<String, Integer> metricValues;
}
