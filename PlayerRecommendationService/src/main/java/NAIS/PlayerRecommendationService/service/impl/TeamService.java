package NAIS.PlayerRecommendationService.service.impl;

import NAIS.PlayerRecommendationService.dto.CreateTeamDto;
import NAIS.PlayerRecommendationService.models.nodes.Team;
import NAIS.PlayerRecommendationService.repository.TeamRepo;
import NAIS.PlayerRecommendationService.service.ITeamService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService implements ITeamService {
    private final TeamRepo teamRepo;

    public TeamService(TeamRepo teamRepo) {
        this.teamRepo = teamRepo;
    }

    public List<Team> getAllTeams() {
        return teamRepo.findAll();
    }

    public Team getTeamById(Long id) {
        return teamRepo.findById(id).orElse(null);
    }

    public Team getTeamByName(String name) {
        return teamRepo.findByName(name);
    }

    public Team saveTeam(CreateTeamDto team) {
        Team newTeam = new Team();
        newTeam.setName(team.getName());
        newTeam.setCountry(team.getCountry());
        return teamRepo.save(newTeam);
    }

    public Team updateTeam(Long id, CreateTeamDto team) {
        Team teamToUpdate = teamRepo.findById(id).orElse(null);
        if (teamToUpdate != null) {
            teamToUpdate.setName(team.getName());
            teamToUpdate.setCountry(team.getCountry());
            return teamRepo.save(teamToUpdate);
        }
        return null;
    }

    @Override
    public boolean deleteTeam(Long id) {
        if (teamRepo.findById(id).isEmpty()) {
            return false;
        }
        teamRepo.deleteById(id);
        return true;
    }
}
