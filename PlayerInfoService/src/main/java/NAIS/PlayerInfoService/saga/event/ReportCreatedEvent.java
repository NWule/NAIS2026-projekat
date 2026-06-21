package NAIS.PlayerInfoService.saga.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class ReportCreatedEvent {
    private String reportId;
    private String text;
    private Integer score;
    private String game;
    private String scoutId;
    private String playerId;
    private Map<Long, Integer> metricValues;
}
