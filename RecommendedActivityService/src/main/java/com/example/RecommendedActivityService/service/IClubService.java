package com.example.RecommendedActivityService.service;

import com.example.RecommendedActivityService.dto.ClubDTO;

import java.util.List;

public interface IClubService {

    ClubDTO createClub(ClubDTO club);

    ClubDTO getById(Integer id);

    List<ClubDTO> getAll();

    ClubDTO getByName(String name);

    ClubDTO updateClub(Integer id, ClubDTO updatedClub);

    void deleteClub(Integer id);

    ClubDTO findBestClub();

    void createParticipation(Integer clubId, Integer matchId, String role);

    void updateParticipation(Integer clubId, Integer matchId, String role,
                             int goalsFor, int goalsAgainst,
                             int possession, int shots);

    void deleteParticipation(Integer clubId, Integer matchId);
}