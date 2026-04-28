package com.example.RecommendedActivityService.service;

import com.example.RecommendedActivityService.dto.MembershipDTO;

import java.util.List;

public interface IMembershipService {

    MembershipDTO create(MembershipDTO membership);

    MembershipDTO getById(Long id);

    List<MembershipDTO> getAll();

    List<MembershipDTO> getByPlayer(Long playerId);

    List<MembershipDTO> getByClub(Long clubId);

    MembershipDTO getCurrentByPlayer(Long playerId);

    MembershipDTO update(Long id, MembershipDTO updated);

    void delete(Long id);

    MembershipDTO transferPlayer(Long playerId, MembershipDTO newMembership);

    void endMembership(Long membershipId);

    void createPerformance(Long membershipId, Long matchId,
                           int goals, int assists, int minutes,
                           int yellow, int red);

    void updatePerformance(Long membershipId, Long matchId,
                           int goals, int assists, int minutes,
                           int yellow, int red);

    void deletePerformance(Long membershipId, Long matchId);
}