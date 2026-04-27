package com.example.RecommendedActivityService.service;

import com.example.RecommendedActivityService.dto.MatchDTO;

import java.util.List;

public interface IMatchService {

    MatchDTO create(MatchDTO match);

    MatchDTO getById(Integer id);

    List<MatchDTO> getAll();

    MatchDTO update(Integer id, MatchDTO match);

    void delete(Integer id);
}