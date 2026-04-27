package com.example.RecommendedActivityService.service.impl;

import com.example.RecommendedActivityService.dto.MatchDTO;
import com.example.RecommendedActivityService.model.Match;
import com.example.RecommendedActivityService.repository.MatchRepository;
import com.example.RecommendedActivityService.service.IMatchService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchService implements IMatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public MatchDTO create(MatchDTO dto) {
        Match match = mapToEntity(dto);
        Match saved = matchRepository.save(match);
        return mapToDTO(saved);
    }

    @Override
    public MatchDTO getById(Integer id) {
        Match match = matchRepository.getById(id);

        if (match == null) {
            throw new RuntimeException("Match not found");
        }

        return mapToDTO(match);
    }

    @Override
    public List<MatchDTO> getAll() {
        return matchRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MatchDTO update(Integer id, MatchDTO dto) {
        Match existing = matchRepository.getById(id);

        if (existing == null) {
            throw new RuntimeException("Match not found");
        }

        existing.setDate(dto.getDate());
        existing.setCompetition(dto.getCompetition());
        existing.setStadium(dto.getStadium());

        Match saved = matchRepository.save(existing);
        return mapToDTO(saved);
    }

    @Override
    public void delete(Integer id) {
        matchRepository.deleteById(id);
    }

    private MatchDTO mapToDTO(Match match) {
        MatchDTO dto = new MatchDTO();
        dto.setMatchId(match.getMatchId());
        dto.setDate(match.getDate());
        dto.setCompetition(match.getCompetition());
        dto.setStadium(match.getStadium());
        return dto;
    }

    private Match mapToEntity(MatchDTO dto) {
        Match match = new Match();
        match.setMatchId(dto.getMatchId());
        match.setDate(dto.getDate());
        match.setCompetition(dto.getCompetition());
        match.setStadium(dto.getStadium());
        return match;
    }
}