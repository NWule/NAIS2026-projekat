package demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import demo.model.MatchEvent;
import demo.service.MatchEventService;

import java.util.List;

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
}