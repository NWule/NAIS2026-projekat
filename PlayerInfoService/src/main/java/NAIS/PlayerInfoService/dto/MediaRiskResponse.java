package NAIS.PlayerInfoService.dto;

import NAIS.PlayerInfoService.model.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaRiskResponse {
    private List<Article> riskyArticles;
    private List<MonthlyRisk> riskTimeline;

    @Data
    @AllArgsConstructor
    public static class MonthlyRisk {
        private String month;
        private Map<String, Long> sourcesCount;
    }
}
