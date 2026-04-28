package NAIS.PlayerRecommendationService.controller;

import NAIS.PlayerRecommendationService.dto.CreateScoutDto;
import NAIS.PlayerRecommendationService.models.nodes.Scout;
import NAIS.PlayerRecommendationService.service.IScoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scouts")
public class ScoutController {

    private final IScoutService scoutService;

    public ScoutController(IScoutService scoutService) {
        this.scoutService = scoutService;
    }

    @GetMapping("")
    public ResponseEntity<List<Scout>> getAllScouts() {
        return ResponseEntity.ok(scoutService.getAllScouts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Scout> getScoutById(@PathVariable Long id) {
        Scout scout = scoutService.getScoutById(id);
        if (scout != null) {
            return ResponseEntity.ok(scout);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("")
    public ResponseEntity<Scout> saveScout(@RequestBody CreateScoutDto newScout) {
        return ResponseEntity.ok(scoutService.saveScout(newScout));
    }

    @PutMapping("")
    public ResponseEntity<Scout> updateScout(@RequestParam Long id, @RequestBody CreateScoutDto updatedScout) {
        Scout scout = scoutService.updateScout(id, updatedScout);
        if (scout != null) {
            return ResponseEntity.ok(scout);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteScout(@RequestParam Long id) {
        if (scoutService.deleteScout(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
