package NAIS.PlayerRecommendationService.service.impl;

import NAIS.PlayerRecommendationService.dto.CreatePlayerDto;
import NAIS.PlayerRecommendationService.models.nodes.Player;
import NAIS.PlayerRecommendationService.models.nodes.Team;
import NAIS.PlayerRecommendationService.models.relationships.TeamMembership;
import NAIS.PlayerRecommendationService.repository.PlayerRepo;
import NAIS.PlayerRecommendationService.repository.TeamRepo;
import NAIS.PlayerRecommendationService.service.IPlayerService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerService implements IPlayerService {

    private final PlayerRepo playerRepo;
    private final TeamRepo teamRepo;

    public PlayerService(PlayerRepo playerRepo, TeamRepo teamRepo) {
        this.teamRepo = teamRepo;
        this.playerRepo = playerRepo;
    }

    public List<Player> getAllPlayers() {
        return playerRepo.findAll();
    }

    public Player getPlayerById(Long id) {
        return playerRepo.findById(id).orElse(null);
    }

    public Player getPlayerByNameAndSurname(String name, String surname) {
        return playerRepo.findByNameAndSurname(name, surname);
    }

    public Player savePlayer(CreatePlayerDto newPlayer) {
        Player player = new Player();
        player.setName(newPlayer.getName());
        player.setSurname(newPlayer.getSurname());
        player.setDateOfBirth(newPlayer.getDateOfBirth());
        player.setNationality(newPlayer.getNationality());
        return playerRepo.save(player);
    }

    public Player updatePlayer(Long id, CreatePlayerDto updatedPlayer) {
        Player player = playerRepo.findById(id).orElse(null);
        if (player != null) {
            player.setName(updatedPlayer.getName());
            player.setSurname(updatedPlayer.getSurname());
            player.setDateOfBirth(updatedPlayer.getDateOfBirth());
            player.setNationality(updatedPlayer.getNationality());
            return playerRepo.save(player);
        }
        return null;
    }

    public boolean deletePlayer(Long id) {
        if (playerRepo.findById(id).isEmpty()) {
            return false;
        }
        playerRepo.deleteById(id);
        return true;
    }

    @Override
    public Player addTeamMembership(Long playerId, Long teamId, String position) {
        Player player = playerRepo.findById(playerId).orElseThrow();
        Team team = teamRepo.findById(teamId).orElseThrow();

        TeamMembership membership = new TeamMembership();
        membership.setTeam(team);
        membership.setStartDate(LocalDate.now());
        membership.setPosition(position);

        if (player.getTeams() == null) {
            player.setTeams(new ArrayList<>());
        }

        player.getTeams().add(membership);
        return playerRepo.save(player);
    }
}
