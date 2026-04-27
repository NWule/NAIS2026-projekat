package com.example.RecommendedActivityService.service.impl;

import com.example.RecommendedActivityService.dto.MembershipDTO;
import com.example.RecommendedActivityService.model.Membership;
import com.example.RecommendedActivityService.repository.MembershipRepository;
import com.example.RecommendedActivityService.service.IMembershipService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembershipService implements IMembershipService {

    private final MembershipRepository membershipRepository;

    public MembershipService(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    @Override
    public MembershipDTO create(MembershipDTO dto) {
        Membership membership = mapToEntity(dto);

        if (membership.getFromDate() == null) {
            membership.setFromDate(LocalDate.now());
        }

        Membership saved = membershipRepository.save(membership);
        return mapToDTO(saved);
    }

    @Override
    public MembershipDTO getById(Integer id) {
        Membership membership = membershipRepository.getById(id);

        if (membership == null) {
            throw new RuntimeException("Membership not found");
        }

        return mapToDTO(membership);
    }

    @Override
    public List<MembershipDTO> getAll() {
        return membershipRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MembershipDTO> getByPlayer(Integer playerId) {
        return membershipRepository.findByPlayerId(playerId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MembershipDTO> getByClub(Integer clubId) {
        return membershipRepository.findByClubId(clubId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MembershipDTO getCurrentByPlayer(Integer playerId) {
        Membership membership = membershipRepository.findCurrentByPlayerId(playerId);

        if (membership == null) {
            throw new RuntimeException("No active membership");
        }

        return mapToDTO(membership);
    }

    @Override
    public MembershipDTO update(Integer id, MembershipDTO updated) {
        Membership existing = membershipRepository.getById(id);

        if (existing == null) {
            throw new RuntimeException("Membership not found");
        }

        existing.setFromDate(updated.getFromDate());
        existing.setToDate(updated.getToDate());
        existing.setJerseyNumber(updated.getJerseyNumber());

        return mapToDTO(membershipRepository.save(existing));
    }

    @Override
    public void delete(Integer id) {
        membershipRepository.deleteById(id);
    }

    @Override
    public MembershipDTO transferPlayer(Integer playerId, MembershipDTO newMembershipDto) {

        Membership current = membershipRepository.findCurrentByPlayerId(playerId);

        if (current != null) {
            current.setToDate(LocalDate.now());
            membershipRepository.save(current);
        }

        Membership newMembership = mapToEntity(newMembershipDto);
        newMembership.setFromDate(LocalDate.now());

        return mapToDTO(membershipRepository.save(newMembership));
    }

    @Override
    public void endMembership(Integer membershipId) {
        Membership membership = membershipRepository.getById(membershipId);

        if (membership == null) {
            throw new RuntimeException("Membership not found");
        }

        membership.setToDate(LocalDate.now());
        membershipRepository.save(membership);
    }

    @Override
    public void createPerformance(Integer membershipId, Integer matchId,
                                  int goals, int assists,
                                  int minutes, int yellow, int red) {

        if (membershipRepository.existsPerformance(membershipId, matchId)) {
            throw new RuntimeException("Performance already exists");
        }

        membershipRepository.createPerformance(
                membershipId, matchId,
                goals, assists, minutes, yellow, red
        );
    }

    @Override
    public void updatePerformance(Integer membershipId, Integer matchId,
                                  int goals, int assists,
                                  int minutes, int yellow, int red) {

        if (!membershipRepository.existsPerformance(membershipId, matchId)) {
            throw new RuntimeException("Performance does not exist");
        }

        membershipRepository.updatePerformance(
                membershipId, matchId,
                goals, assists, minutes, yellow, red
        );
    }

    @Override
    public void deletePerformance(Integer membershipId, Integer matchId) {

        if (!membershipRepository.existsPerformance(membershipId, matchId)) {
            throw new RuntimeException("Performance does not exist");
        }

        membershipRepository.deletePerformance(membershipId, matchId);
    }

    private MembershipDTO mapToDTO(Membership m) {
        MembershipDTO dto = new MembershipDTO();
        dto.setId(m.getId());
        dto.setFromDate(m.getFromDate());
        dto.setToDate(m.getToDate());
        dto.setJerseyNumber(m.getJerseyNumber());

        if (m.getPlayer() != null) {
            dto.setPlayerId(m.getPlayer().getPlayerId());
        }

        if (m.getClub() != null) {
            dto.setClubId(m.getClub().getClubId());
        }

        return dto;
    }

    private Membership mapToEntity(MembershipDTO dto) {
        Membership m = new Membership();
        m.setId(dto.getId());
        m.setFromDate(dto.getFromDate());
        m.setToDate(dto.getToDate());
        m.setJerseyNumber(dto.getJerseyNumber());

        return m;
    }
}