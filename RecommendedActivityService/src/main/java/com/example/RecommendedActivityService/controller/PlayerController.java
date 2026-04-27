package com.example.RecommendedActivityService.controller;

import com.example.RecommendedActivityService.dto.PlayerDTO;
import com.example.RecommendedActivityService.service.IPlayerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final IPlayerService playerService;

    public PlayerController(IPlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public PlayerDTO create(@RequestBody PlayerDTO dto) {
        return playerService.create(dto);
    }

    @GetMapping("/{id}")
    public PlayerDTO getById(@PathVariable Integer id) {
        return playerService.getById(id);
    }

    @GetMapping
    public List<PlayerDTO> getAll() {
        return playerService.getAll();
    }

    @PutMapping("/{id}")
    public PlayerDTO update(@PathVariable Integer id,
                            @RequestBody PlayerDTO dto) {
        return playerService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        playerService.delete(id);
    }

    @GetMapping("/search/name/{name}")
    public List<PlayerDTO> findByName(@PathVariable String name) {
        return playerService.findByName(name);
    }

    @GetMapping("/search/surname/{surname}")
    public List<PlayerDTO> findBySurname(@PathVariable String surname) {
        return playerService.findBySurname(surname);
    }

    @GetMapping("/analysis/top-scorer-home")
    public PlayerDTO topScorerAtHome() {
        return playerService.findTopScorerAtHomeMatches();
    }

    @GetMapping("/analysis/most-red-cards-lost")
    public PlayerDTO mostRedCardsInLostMatches() {
        return playerService.findMostRedCardsInLostMatches();
    }
}