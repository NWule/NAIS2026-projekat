package demo.service;

import org.springframework.stereotype.Service;
import demo.model.MatchEvent;
import demo.repository.MatchEventRepositoryImpl;

import java.util.List;


@Service
public class MatchEventService {

    private final MatchEventRepositoryImpl matchEventRepository;

    public MatchEventService(MatchEventRepositoryImpl matchEventRepository) {
        this.matchEventRepository = matchEventRepository;
    }

    public boolean save(MatchEvent matchEvent) {
        return matchEventRepository.save(matchEvent);
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
}