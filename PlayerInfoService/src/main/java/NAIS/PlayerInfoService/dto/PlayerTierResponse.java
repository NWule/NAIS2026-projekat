package NAIS.PlayerInfoService.dto;

import NAIS.PlayerInfoService.model.Report;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerTierResponse {
    private List<Report> matchingReports;
    private List<TierStat> tierStatistics;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TierStat {
        private String tierName;
        private long playerCount;
        private List<String> topTags;
    }
}
