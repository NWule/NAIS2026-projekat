package demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.model.MatchEvent;
import demo.service.MatchEventService;

@RestController
@RequestMapping("/matchEvent.json")
public class MatchEventController {

    private final MatchEventService matchEventService;

    public MatchEventController(MatchEventService matchEventService) {
        this.matchEventService = matchEventService;
    }

    @GetMapping("findAll")
    public ResponseEntity<List<MatchEvent>> findAll() {
        return new ResponseEntity<>(matchEventService.findAll(), HttpStatus.OK);
    }

    @GetMapping("findAllByMatchId")
    public ResponseEntity<List<MatchEvent>> findAllByMatchId(@RequestParam("matchId") String matchId) {
        return new ResponseEntity<>(matchEventService.findAllByMatchId(matchId), HttpStatus.OK);
    }

    @GetMapping("findAllByPlayerId")
    public ResponseEntity<List<MatchEvent>> findAllByPlayerId(@RequestParam("playerId") String playerId) {
        return new ResponseEntity<>(matchEventService.findAllByPlayerId(playerId), HttpStatus.OK);
    }

    @PostMapping("save")
    public ResponseEntity<Boolean> save(@RequestBody MatchEvent matchEvent) {
        if (matchEventService.save(matchEvent)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("deleteRecord")
    public ResponseEntity<Boolean> deleteRecord(@RequestParam("playerId") String playerId) {
        if (matchEventService.deleteRecord(playerId)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("getLiveStats")
    public ResponseEntity<Map<String, Map<String, Long>>> getLiveStats(@RequestParam("matchId") String matchId) {
        return new ResponseEntity<>(matchEventService.getLiveStats(matchId), HttpStatus.OK);
    }

    @GetMapping("getTopScorers")
    public ResponseEntity<Map<String, Long>> getTopScorers() {
        return new ResponseEntity<>(matchEventService.getTopScorers(), HttpStatus.OK);
    }

    @GetMapping("getTeamFoulsByMatch")
    public ResponseEntity<Map<String, Long>> getTeamFoulsByMatch(@RequestParam("matchId") String matchId) {
        return new ResponseEntity<>(matchEventService.getTeamFoulsByMatch(matchId), HttpStatus.OK);
    }

    @GetMapping("getMostPassesInSingleMatch")
    public ResponseEntity<Map<String, Long>> getMostPassesInSingleMatch() {
        return new ResponseEntity<>(matchEventService.getMostPassesInSingleMatch(), HttpStatus.OK);
    }

    @PostMapping("seed-data")
    public ResponseEntity<String> seedData(@RequestParam(defaultValue = "2000") int count) {

        Map<String, List<String>> matchClubsMap = new HashMap<>();
        matchClubsMap.put("match_001", List.of("club_01", "club_02"));
        matchClubsMap.put("match_002", List.of("club_03", "club_04"));
        matchClubsMap.put("match_003", List.of("club_01", "club_03"));
        matchClubsMap.put("match_004", List.of("club_02", "club_04"));
        matchClubsMap.put("match_005", List.of("club_01", "club_04"));

        String[] eventTypes = {"pass", "pass", "pass", "foul", "goal", "red_card"};
        List<String> matchIds = new ArrayList<>(matchClubsMap.keySet()); // Izvlačimo listu mečeva

        java.util.Random random = new java.util.Random();

        for (int i = 0; i < count; i++) {
            String matchId = matchIds.get(random.nextInt(matchIds.size()));

            List<String> allowedClubs = matchClubsMap.get(matchId);

            String clubId = allowedClubs.get(random.nextInt(allowedClubs.size()));

            MatchEvent event = new MatchEvent();
            event.setMatchId(matchId);
            event.setPlayerId("player_" + random.nextInt(50)); 
            event.setClubId(clubId);
            event.setEventType(eventTypes[random.nextInt(eventTypes.length)]);

            event.set_field("count");
            event.set_value(1.0);

            matchEventService.save(event);
        }

        return ResponseEntity.ok("Uspešno ubačeno " + count + " logičnih događaja u bazu.");
    }
}