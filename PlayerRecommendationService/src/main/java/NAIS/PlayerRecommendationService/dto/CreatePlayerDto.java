package NAIS.PlayerRecommendationService.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreatePlayerDto {
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String nationality;
}
