package NAIS.PlayerRecommendationService.service;

import NAIS.PlayerRecommendationService.dto.CreatePlayerDto;
import NAIS.PlayerRecommendationService.dto.CreateTeamDto;
import NAIS.PlayerRecommendationService.models.nodes.Team;

import java.util.List;

public interface ITeamService {

    public List<Team> getAllTeams();

    public Team getTeamById(Long id);

    public Team getTeamByName(String name);

    public Team saveTeam(CreateTeamDto newTeam);

    public Team updateTeam(Long id, CreateTeamDto updatedTeam);

    public boolean deleteTeam(Long id);
}
