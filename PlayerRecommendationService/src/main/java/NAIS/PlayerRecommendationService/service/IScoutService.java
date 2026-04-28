package NAIS.PlayerRecommendationService.service;

import NAIS.PlayerRecommendationService.dto.CreateScoutDto;
import NAIS.PlayerRecommendationService.models.nodes.Scout;

import java.util.List;

public interface IScoutService {

    public List<Scout> getAllScouts();

    public Scout getScoutById(Long id);

    public Scout getScoutByNameAndSurname(String name, String surname);

    public Scout saveScout(CreateScoutDto newScout);

    public Scout updateScout(Long id, CreateScoutDto updatedScout);

    public boolean deleteScout(Long id);
}
