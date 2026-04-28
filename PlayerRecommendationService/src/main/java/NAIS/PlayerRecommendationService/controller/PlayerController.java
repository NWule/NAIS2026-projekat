package NAIS.PlayerRecommendationService.controller;

import NAIS.PlayerRecommendationService.dto.CreatePlayerDto;
import NAIS.PlayerRecommendationService.models.nodes.Player;
import NAIS.PlayerRecommendationService.service.impl.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    public final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("")
    public ResponseEntity<List<Player>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id) {
        Player player = playerService.getPlayerById(id);
        if (player != null) {
            return ResponseEntity.ok(player);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("")
    public ResponseEntity<Player> savePlayer(CreatePlayerDto newPlayer) {
        return ResponseEntity.ok(playerService.savePlayer(newPlayer));
    }

    @PutMapping("")
    public ResponseEntity<Player> updatePlayer(@RequestParam Long id, CreatePlayerDto updatedPlayer) {
        return ResponseEntity.ok(playerService.updatePlayer(id, updatedPlayer));
    }

    @DeleteMapping("")
    public ResponseEntity<Player> deletePlayer(@RequestParam Long id) {
        if (playerService.deletePlayer(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/teams")
    public ResponseEntity<Player> addTeamToPlayer(
            @PathVariable Long id,
            @RequestParam Long teamId,
            @RequestParam String position) {

        Player updatedPlayer = playerService.addTeamMembership(id, teamId, position);

        return ResponseEntity.ok(updatedPlayer);
    }

    @PutMapping("/{id}/teams/end")
    public ResponseEntity<Void> endTeamMembership(
            @PathVariable Long id,
            @RequestParam Long teamId) {

        playerService.endPlayerMembership(id, teamId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/best-per-metrics")
    public ResponseEntity<Player> getBestPlayerForMetrics(@RequestParam List<Long> metricIds) {
        Player player = playerService.getPlayerWithBestScoreForMetrics(metricIds);
        if (player != null) {
            return ResponseEntity.ok(player);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/similar")
    public ResponseEntity<List<Player>> getSimilarPlayers(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.getSimilarPlayers(id));
    }

    @GetMapping("/wonderkids")
    public ResponseEntity<List<Player>> getWonderkids(
            @RequestParam Integer maxAge,
            @RequestParam Integer minScoutReliability,
            @RequestParam Double minScore) {

        List<Player> prospects = playerService.getEliteProspects(maxAge, minScoutReliability, minScore);

        if (prospects.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(prospects);
    }
}
