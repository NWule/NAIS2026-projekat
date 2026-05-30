package NAIS.PlayerInfoService.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.Instant;
import java.util.List;

@Document(indexName = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    private String reportId;

    @Field(type = FieldType.Keyword)
    private String playerId;

    @Field(type = FieldType.Keyword)
    private String scoutId;

    @Field(type = FieldType.Text, analyzer = "english")
    private String psychologicalProfile;

    @Field(type = FieldType.Text, analyzer = "english")
    private String tacticalNotes;

    @Field(type = FieldType.Text, analyzer = "english")
    private String weaknesses;

    @Field(type = FieldType.Keyword)
    private List<String> tags;

    private Integer overallRating;
    private Instant createdAt;
}
