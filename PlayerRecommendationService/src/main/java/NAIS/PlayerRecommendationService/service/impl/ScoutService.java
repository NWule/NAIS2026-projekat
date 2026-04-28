package NAIS.PlayerRecommendationService.service.impl;

import NAIS.PlayerRecommendationService.dto.CreateScoutDto;
import NAIS.PlayerRecommendationService.models.nodes.Scout;
import NAIS.PlayerRecommendationService.repository.ScoutRepo;
import NAIS.PlayerRecommendationService.service.IScoutService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoutService implements IScoutService {
    private final ScoutRepo scoutRepo;

    public ScoutService(ScoutRepo scoutRepo) {
        this.scoutRepo = scoutRepo;
    }

    @Override
    public List<Scout> getAllScouts() {
        return scoutRepo.findAll();
    }

    @Override
    public Scout getScoutById(Long id) {
        return scoutRepo.findById(id).orElse(null);
    }

    @Override
    public Scout getScoutByNameAndSurname(String name, String surname) {
        return scoutRepo.findByNameAndSurname(name, surname);
    }

    @Override
    public Scout saveScout(CreateScoutDto scoutDto) {
        Scout scout = new Scout();
        scout.setName(scoutDto.getName());
        scout.setSurname(scoutDto.getSurname());
        scout.setYearsOfExperience(scoutDto.getYearsOfExperience());
        scout.setReliabilityScore(scoutDto.getReliabilityRating());
        return scoutRepo.save(scout);
    }

    @Override
    public Scout updateScout(Long id, CreateScoutDto scoutDto) {
        Scout scoutToUpdate = scoutRepo.findById(id).orElse(null);
        if (scoutToUpdate != null) {
            scoutToUpdate.setName(scoutDto.getName());
            scoutToUpdate.setSurname(scoutDto.getSurname());
            scoutToUpdate.setYearsOfExperience(scoutDto.getYearsOfExperience());
            scoutToUpdate.setReliabilityScore(scoutDto.getReliabilityRating());
            return scoutRepo.save(scoutToUpdate);
        }
        return null;
    }

    @Override
    public boolean deleteScout(Long id) {
        if (scoutRepo.findById(id).isEmpty()) {
            return false;
        }
        scoutRepo.deleteById(id);
        return true;
    }
}
