package NAIS.PlayerRecommendationService.service;

import NAIS.PlayerRecommendationService.dto.CreatePlayerDto;
import NAIS.PlayerRecommendationService.models.nodes.Player;

import java.time.LocalDate;
import java.util.List;

public interface IPlayerService {

    public List<Player> getAllPlayers();

    public Player getPlayerById(String id);

    public Player getPlayerByNameAndSurname(String name, String surname);

    public Player savePlayer(CreatePlayerDto newPlayer);

    public Player updatePlayer(String id, CreatePlayerDto updatedPlayer);

    public boolean deletePlayer(String id);

    Player addTeamMembership(String playerId, String teamId, String position);

    public void endPlayerMembership(String playerId, String teamId);

    public Player getPlayerWithBestScoreForMetrics(List<String> metricIds);

    List<Player> getSimilarPlayers(String id);

    List<Player> getEliteProspects(Integer maxAge, Integer minScoutReliability, Double minScore);
}
