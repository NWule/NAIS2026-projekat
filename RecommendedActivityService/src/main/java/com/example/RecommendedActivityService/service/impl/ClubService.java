package com.example.RecommendedActivityService.service.impl;

import com.example.RecommendedActivityService.dto.ClubDTO;
import com.example.RecommendedActivityService.model.Club;
import com.example.RecommendedActivityService.repository.ClubRepository;
import com.example.RecommendedActivityService.service.IClubService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClubService implements IClubService {

    private final ClubRepository clubRepository;

    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Override
    public ClubDTO createClub(ClubDTO dto) {
        Club club = mapToEntity(dto);
        Club saved = clubRepository.save(club);
        return mapToDTO(saved);
    }

    @Override
    public ClubDTO getById(Integer id) {
        Club club = clubRepository.getById(id);

        if (club == null) {
            throw new RuntimeException("Club not found");
        }

        return mapToDTO(club);
    }

    @Override
    public List<ClubDTO> getAll() {
        return clubRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClubDTO getByName(String name) {
        Club club = clubRepository.findByName(name);

        if (club == null) {
            throw new RuntimeException("Club not found");
        }

        return mapToDTO(club);
    }

    @Override
    public ClubDTO updateClub(Integer id, ClubDTO dto) {
        Club existing = clubRepository.getById(id);

        if (existing == null) {
            throw new RuntimeException("Club not found");
        }

        existing.setName(dto.getName());
        existing.setCrest(dto.getCrest());
        existing.setWins(dto.getWins());
        existing.setLosses(dto.getLosses());
        existing.setDraws(dto.getDraws());

        Club saved = clubRepository.save(existing);
        return mapToDTO(saved);
    }

    @Override
    public void deleteClub(Integer id) {
        clubRepository.deleteById(id);
    }

    @Override
    public ClubDTO findBestClub() {
        Club club = clubRepository.findBestLowPossessionDiverseScoringClub();

        if (club == null) {
            throw new RuntimeException("No club found");
        }

        return mapToDTO(club);
    }

    @Override
    public void createParticipation(Integer clubId, Integer matchId, String role) {
        clubRepository.createParticipation(clubId, matchId, role);
    }

    @Override
    public void updateParticipation(Integer clubId, Integer matchId, String role,
                                    int goalsFor, int goalsAgainst,
                                    int possession, int shots) {
        clubRepository.updateParticipation(clubId, matchId, role, goalsFor, goalsAgainst, possession, shots);
    }

    @Override
    public void deleteParticipation(Integer clubId, Integer matchId) {
        clubRepository.deleteParticipation(clubId, matchId);
    }

    private ClubDTO mapToDTO(Club club) {
        ClubDTO dto = new ClubDTO();
        dto.setClubId(club.getClubId());
        dto.setName(club.getName());
        dto.setCrest(club.getCrest());
        dto.setWins(club.getWins());
        dto.setLosses(club.getLosses());
        dto.setDraws(club.getDraws());
        return dto;
    }

    private Club mapToEntity(ClubDTO dto) {
        Club club = new Club();
        club.setClubId(dto.getClubId());
        club.setName(dto.getName());
        club.setCrest(dto.getCrest());
        club.setWins(dto.getWins());
        club.setLosses(dto.getLosses());
        club.setDraws(dto.getDraws());
        return club;
    }
}