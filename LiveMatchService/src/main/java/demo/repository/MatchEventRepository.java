package demo.repository;

import demo.model.MatchEvent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MatchEventRepository {

    Boolean save(MatchEvent matchEvent);

    List<MatchEvent> findAllByMatchId(String matchId);

    List<MatchEvent> findAllByPlayerId(String playerId);

    List<MatchEvent> retrieveDataFromInfluxDB();

    Boolean deleteRecord(String playerId);

    Map<String, Long> findTopScorers();
    Map<String, Long> findTeamFoulsByMatch(String matchId);
    Map<String, Long> findMostPassesInSingleMatch();

    Boolean markMatchAsDeleted(String matchId);
}
