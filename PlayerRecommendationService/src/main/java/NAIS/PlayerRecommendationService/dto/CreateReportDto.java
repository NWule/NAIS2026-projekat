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
    private Long scoutId;
    private Long playerId;
    private Map<Long, Integer> metricValues;
}
