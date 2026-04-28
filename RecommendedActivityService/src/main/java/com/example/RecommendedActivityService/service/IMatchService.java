package com.example.RecommendedActivityService.service;

import com.example.RecommendedActivityService.dto.MatchDTO;

import java.util.List;

public interface IMatchService {

    MatchDTO create(MatchDTO match);

    MatchDTO getById(Long id);

    List<MatchDTO> getAll();

    MatchDTO update(Long id, MatchDTO match);

    void delete(Long id);
}