package com.example.RecommendedActivityService.controller;

import com.example.RecommendedActivityService.dto.MembershipDTO;
import com.example.RecommendedActivityService.service.IMembershipService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    private final IMembershipService membershipService;

    public MembershipController(IMembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @PostMapping
    public MembershipDTO create(@RequestBody MembershipDTO dto) {
        return membershipService.create(dto);
    }

    @GetMapping("/{id}")
    public MembershipDTO getById(@PathVariable Long id) {
        return membershipService.getById(id);
    }

    @GetMapping
    public List<MembershipDTO> getAll() {
        return membershipService.getAll();
    }

    @GetMapping("/player/{playerId}")
    public List<MembershipDTO> getByPlayer(@PathVariable Long playerId) {
        return membershipService.getByPlayer(playerId);
    }

    @GetMapping("/club/{clubId}")
    public List<MembershipDTO> getByClub(@PathVariable Long clubId) {
        return membershipService.getByClub(clubId);
    }

    @GetMapping("/player/{playerId}/current")
    public MembershipDTO getCurrentByPlayer(@PathVariable Long playerId) {
        return membershipService.getCurrentByPlayer(playerId);
    }

    @PutMapping("/{id}")
    public MembershipDTO update(@PathVariable Long id,
                                @RequestBody MembershipDTO dto) {
        return membershipService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        membershipService.delete(id);
    }

    @PostMapping("/{membershipId}/performance")
    public void createPerformance(
            @PathVariable Long membershipId,
            @RequestParam Long matchId,
            @RequestParam int goals,
            @RequestParam int assists,
            @RequestParam int minutes,
            @RequestParam int yellow,
            @RequestParam int red
    ) {
        membershipService.createPerformance(membershipId, matchId, goals, assists, minutes, yellow, red);
    }

    @PutMapping("/{membershipId}/performance")
    public void updatePerformance(
            @PathVariable Long membershipId,
            @RequestParam Long matchId,
            @RequestParam int goals,
            @RequestParam int assists,
            @RequestParam int minutes,
            @RequestParam int yellow,
            @RequestParam int red
    ) {
        membershipService.updatePerformance(membershipId, matchId, goals, assists, minutes, yellow, red);
    }

    @DeleteMapping("/{membershipId}/performance")
    public void deletePerformance(
            @PathVariable Long membershipId,
            @RequestParam Long matchId
    ) {
        membershipService.deletePerformance(membershipId, matchId);
    }
}