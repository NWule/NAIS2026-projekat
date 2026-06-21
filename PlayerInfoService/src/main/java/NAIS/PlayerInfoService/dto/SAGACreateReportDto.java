package NAIS.PlayerInfoService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SAGACreateReportDto {
    private String psychologicalProfile;
    private String tacticalNotes;
    private String weaknesses;
    private List<String> tags;
    private Integer overallRating;
    private String game;
    private String scoutId;
    private String playerId;
    private Map<String, Integer> metricValues;
}
