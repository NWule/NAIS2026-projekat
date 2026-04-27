package com.example.RecommendedActivityService.controller;

import com.example.RecommendedActivityService.dto.ClubDTO;
import com.example.RecommendedActivityService.service.IClubService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clubs")
public class ClubController {

    private final IClubService clubService;

    public ClubController(IClubService clubService) {
        this.clubService = clubService;
    }

    @PostMapping
    public ClubDTO create(@RequestBody ClubDTO dto) {
        return clubService.createClub(dto);
    }

    @GetMapping("/{id}")
    public ClubDTO getById(@PathVariable Integer id) {
        return clubService.getById(id);
    }

    @GetMapping
    public List<ClubDTO> getAll() {
        return clubService.getAll();
    }

    @GetMapping("/name/{name}")
    public ClubDTO getByName(@PathVariable String name) {
        return clubService.getByName(name);
    }

    @PutMapping("/{id}")
    public ClubDTO update(@PathVariable Integer id,
                          @RequestBody ClubDTO dto) {
        return clubService.updateClub(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        clubService.deleteClub(id);
    }

    @PostMapping("/{clubId}/participation")
    public void createParticipation(
            @PathVariable Integer clubId,
            @RequestParam Integer matchId,
            @RequestParam String role
    ) {
        clubService.createParticipation(clubId, matchId, role);
    }

    @PutMapping("/{clubId}/participation")
    public void updateParticipation(
            @PathVariable Integer clubId,
            @RequestParam Integer matchId,
            @RequestParam String role,
            @RequestParam int goalsFor,
            @RequestParam int goalsAgainst,
            @RequestParam int possession,
            @RequestParam int shots
    ) {
        clubService.updateParticipation(clubId, matchId, role, goalsFor, goalsAgainst, possession, shots);
    }

    @DeleteMapping("/{clubId}/participation")
    public void deleteParticipation(
            @PathVariable Integer clubId,
            @RequestParam Integer matchId
    ) {
        clubService.deleteParticipation(clubId, matchId);
    }

    @GetMapping("/analysis/best")
    public ClubDTO findBestClub() {
        return clubService.findBestClub();
    }
}