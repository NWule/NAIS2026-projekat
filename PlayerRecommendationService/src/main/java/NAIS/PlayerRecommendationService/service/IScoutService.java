package NAIS.PlayerRecommendationService.service;

import NAIS.PlayerRecommendationService.dto.CreateScoutDto;
import NAIS.PlayerRecommendationService.models.nodes.Scout;

import java.util.List;

public interface IScoutService {

    public List<Scout> getAllScouts();

    public Scout getScoutById(String id);

    public Scout getScoutByNameAndSurname(String name, String surname);

    public Scout saveScout(CreateScoutDto newScout);

    public Scout updateScout(String id, CreateScoutDto updatedScout);

    public boolean deleteScout(String id);
}
