package NAIS.PlayerInfoService.dto;

import NAIS.PlayerInfoService.model.Report;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TacticalTrendResponse {
    private List<Report> topTacticalProspects;
    private List<ScoutMaxRating> scoutPerformances;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScoutMaxRating {
        private String scoutId;
        private double maxRating;
        private String topPlayerId;
    }
}
