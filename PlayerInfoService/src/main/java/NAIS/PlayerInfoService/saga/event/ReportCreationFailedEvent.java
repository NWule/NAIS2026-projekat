package NAIS.PlayerInfoService.saga.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ReportCreationFailedEvent {
    private String reason;
    private String reportId;
}
