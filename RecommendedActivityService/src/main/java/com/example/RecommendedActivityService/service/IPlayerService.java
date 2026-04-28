package com.example.RecommendedActivityService.service;

import com.example.RecommendedActivityService.dto.PlayerDTO;

import java.util.List;

public interface IPlayerService {

    PlayerDTO create(PlayerDTO player);

    List<PlayerDTO> getAll();

    PlayerDTO getById(Long id);

    PlayerDTO update(Long id, PlayerDTO updatedPlayer);

    void delete(Long id);

    List<PlayerDTO> findByName(String name);

    List<PlayerDTO> findBySurname(String surname);

    PlayerDTO findTopScorerAtHomeMatches();

    PlayerDTO findMostRedCardsInLostMatches();
}