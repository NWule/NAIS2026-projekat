package NAIS.PlayerRecommendationService.service;

import NAIS.PlayerRecommendationService.dto.CreatePlayerDto;
import NAIS.PlayerRecommendationService.models.nodes.Player;

import java.time.LocalDate;
import java.util.List;

public interface IPlayerService {

    public List<Player> getAllPlayers();

    public Player getPlayerById(Long id);

    public Player getPlayerByNameAndSurname(String name, String surname);

    public Player savePlayer(CreatePlayerDto newPlayer);

    public Player updatePlayer(Long id, CreatePlayerDto updatedPlayer);

    public boolean deletePlayer(Long id);

    Player addTeamMembership(Long playerId, Long teamId, String position);

    public void endPlayerMembership(Long playerId, Long teamId);

    public Player getPlayerWithBestScoreForMetrics(List<Long> metricIds);

    List<Player> getSimilarPlayers(Long id);

    List<Player> getEliteProspects(Integer maxAge, Integer minScoutReliability, Double minScore);
}
