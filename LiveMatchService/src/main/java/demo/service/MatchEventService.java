package demo.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import demo.model.MatchEvent;
import demo.repository.MatchEventRepositoryImpl;

import java.util.List;
import java.util.Map;


@Service
public class MatchEventService {

    private final MatchEventRepositoryImpl matchEventRepository;
    private final StringRedisTemplate redisTemplate;

    public MatchEventService(MatchEventRepositoryImpl matchEventRepository, StringRedisTemplate redisTemplate) {
        this.matchEventRepository = matchEventRepository;
        this.redisTemplate = redisTemplate;
    }

    public boolean save(MatchEvent matchEvent) {
        boolean savedToInflux = matchEventRepository.save(matchEvent);

        if (savedToInflux && matchEvent.getMatchId() != null) {
            String redisKey = "match:" + matchEvent.getMatchId() + ":stats";
            String eventType = matchEvent.getEventType();

            if (eventType != null) {
                redisTemplate.opsForHash().increment(redisKey, eventType.toLowerCase(), 1);
            }
        }

        return savedToInflux;
    }

    public List<MatchEvent> findAll() {
        return matchEventRepository.retrieveDataFromInfluxDB();
    }

    public List<MatchEvent> findAllByMatchId(String matchId) {
        return matchEventRepository.findAllByMatchId(matchId);
    }

    public List<MatchEvent> findAllByPlayerId(String playerId) {
        return matchEventRepository.findAllByPlayerId(playerId);
    }

    public boolean deleteRecord(String playerId) {
        return matchEventRepository.deleteRecord(playerId);
    }

    public Map<Object, Object> getLiveStats(String matchId) {
        String redisKey = "match:" + matchId + ":stats";
        return redisTemplate.opsForHash().entries(redisKey);
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