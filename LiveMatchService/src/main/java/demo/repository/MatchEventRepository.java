package demo.repository;

import demo.model.MatchEvent;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchEventRepository {

    Boolean save(MatchEvent matchEvent);

    List<MatchEvent> findAllByMatchId(String matchId);

    List<MatchEvent> findAllByPlayerId(String playerId);

    List<MatchEvent> retrieveDataFromInfluxDB();

    Boolean deleteRecord(String playerId);
}
