package NAIS.PlayerRecommendationService.service;

import NAIS.PlayerRecommendationService.dto.CreatePlayerDto;
import NAIS.PlayerRecommendationService.dto.CreateTeamDto;
import NAIS.PlayerRecommendationService.models.nodes.Team;

import java.util.List;

public interface ITeamService {

    public List<Team> getAllTeams();

    public Team getTeamById(String id);

    public Team getTeamByName(String name);

    public Team saveTeam(CreateTeamDto newTeam);

    public Team updateTeam(String id, CreateTeamDto updatedTeam);

    public boolean deleteTeam(String id);
}
