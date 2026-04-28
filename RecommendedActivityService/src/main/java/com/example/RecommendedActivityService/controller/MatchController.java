package com.example.RecommendedActivityService.controller;

import com.example.RecommendedActivityService.dto.MatchDTO;
import com.example.RecommendedActivityService.service.IMatchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final IMatchService matchService;

    public MatchController(IMatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping
    public MatchDTO create(@RequestBody MatchDTO dto) {
        return matchService.create(dto);
    }

    @GetMapping("/{id}")
    public MatchDTO getById(@PathVariable Long id) {
        return matchService.getById(id);
    }

    @GetMapping
    public List<MatchDTO> getAll() {
        return matchService.getAll();
    }

    @PutMapping("/{id}")
    public MatchDTO update(@PathVariable Long id,
                           @RequestBody MatchDTO dto) {
        return matchService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        matchService.delete(id);
    }
}