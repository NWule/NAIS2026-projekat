package NAIS.PlayerInfoService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {
    private String playerId;

    private String title;

    private String content;

    private String source;

    private Double sentimentScore;

    private Instant publishDate;
}
