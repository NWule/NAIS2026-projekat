package NAIS.PlayerInfoService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private String playerId;

    private String scoutId;

    private String psychologicalProfile;

    private String tacticalNotes;

    private String weaknesses;

    private List<String> tags;

    private Integer overallRating;
}
