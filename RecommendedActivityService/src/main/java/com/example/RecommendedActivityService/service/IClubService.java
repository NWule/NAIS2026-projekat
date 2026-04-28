package com.example.RecommendedActivityService.service;

import com.example.RecommendedActivityService.dto.ClubDTO;

import java.util.List;

public interface IClubService {

    ClubDTO createClub(ClubDTO club);

    ClubDTO getById(Long id);

    List<ClubDTO> getAll();

    ClubDTO getByName(String name);

    ClubDTO updateClub(Long id, ClubDTO updatedClub);

    void deleteClub(Long id);

    ClubDTO findBestClub();

    void createParticipation(Long clubId, Long matchId, String role);

    void updateParticipation(Long clubId, Long matchId, String role,
                             int goalsFor, int goalsAgainst,
                             int possession, int shots);

    void deleteParticipation(Long clubId, Long matchId);
}