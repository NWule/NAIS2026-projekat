package NAIS.PlayerRecommendationService.controller;

import NAIS.PlayerRecommendationService.dto.CreateTeamDto;
import NAIS.PlayerRecommendationService.models.nodes.Team;
import NAIS.PlayerRecommendationService.service.impl.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("")
    public ResponseEntity<List<Team>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        Team team = teamService.getTeamById(id);
        if (team != null) {
            return ResponseEntity.ok(team);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("")
    public ResponseEntity<Team> saveTeam(@RequestBody CreateTeamDto newTeam) {
        return ResponseEntity.ok(teamService.saveTeam(newTeam));
    }

    @PutMapping("")
    public ResponseEntity<Team> updateTeam(@RequestParam Long id, @RequestBody CreateTeamDto updatedTeam) {
        Team team = teamService.updateTeam(id, updatedTeam);
        if (team != null) {
            return ResponseEntity.ok(team);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteTeam(@RequestParam Long id) {
        if (teamService.deleteTeam(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
