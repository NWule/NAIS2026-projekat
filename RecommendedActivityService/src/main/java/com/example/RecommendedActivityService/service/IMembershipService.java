package com.example.RecommendedActivityService.service;

import com.example.RecommendedActivityService.dto.MembershipDTO;

import java.util.List;

public interface IMembershipService {

    MembershipDTO create(MembershipDTO membership);

    MembershipDTO getById(Integer id);

    List<MembershipDTO> getAll();

    List<MembershipDTO> getByPlayer(Integer playerId);

    List<MembershipDTO> getByClub(Integer clubId);

    MembershipDTO getCurrentByPlayer(Integer playerId);

    MembershipDTO update(Integer id, MembershipDTO updated);

    void delete(Integer id);

    MembershipDTO transferPlayer(Integer playerId, MembershipDTO newMembership);

    void endMembership(Integer membershipId);

    void createPerformance(Integer membershipId, Integer matchId,
                           int goals, int assists, int minutes,
                           int yellow, int red);

    void updatePerformance(Integer membershipId, Integer matchId,
                           int goals, int assists, int minutes,
                           int yellow, int red);

    void deletePerformance(Integer membershipId, Integer matchId);
}