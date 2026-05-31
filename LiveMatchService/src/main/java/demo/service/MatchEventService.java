package demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import demo.model.MatchEvent;
import demo.repository.MatchEventRepositoryImpl;

@Service
@EnableCaching
public class MatchEventService {

    private final MatchEventRepositoryImpl matchEventRepository;

    public MatchEventService(MatchEventRepositoryImpl matchEventRepository) {
        this.matchEventRepository = matchEventRepository;
    }


    @CacheEvict(value = "matchEvents", key = "#matchEvent.matchId",
            condition = "#matchEvent.eventType != null and (#matchEvent.eventType.toLowerCase() == 'goal' or #matchEvent.eventType.toLowerCase() == 'red_card')"
    )
    public boolean save(MatchEvent matchEvent) {
        return matchEventRepository.save(matchEvent);
    }

    public List<MatchEvent> findAll() {
        return matchEventRepository.retrieveDataFromInfluxDB();
    }

    @Cacheable(value = "matchEvents", key = "#matchId", condition = "#matchId != null")
    public List<MatchEvent> findAllByMatchId(String matchId) {
        return matchEventRepository.findAllByMatchId(matchId);
    }

    public List<MatchEvent> findAllByPlayerId(String playerId) {
        return matchEventRepository.findAllByPlayerId(playerId);
    }

    public boolean deleteRecord(String playerId) {
        return matchEventRepository.deleteRecord(playerId);
    }


    public Map<String, Map<String, Long>> getLiveStats(String matchId) {
    List<MatchEvent> events = findAllByMatchId(matchId);
    
    Map<String, Map<String, Long>> stats = new HashMap<>();

    if (events != null) {
        for (MatchEvent event : events) {
            String clubId = event.getClubId();
            String type = (event.getEventType() != null) ? event.getEventType().toLowerCase() : "unknown";

            stats.putIfAbsent(clubId, new HashMap<>());
            
            Map<String, Long> clubStats = stats.get(clubId);
            clubStats.put(type, clubStats.getOrDefault(type, 0L) + 1);
        }
    }
    return stats;
}

    public Map<String, Long> getTopScorers() {
        return matchEventRepository.findTopScorers();
    }

    public Map<String, Long> getTeamFoulsByMatch(String matchId) {
        return matchEventRepository.findTeamFoulsByMatch(matchId);
    }

    public Map<String, Long> getMostPassesInSingleMatch() {
        return matchEventRepository.findMostPassesInSingleMatch();
    }
}