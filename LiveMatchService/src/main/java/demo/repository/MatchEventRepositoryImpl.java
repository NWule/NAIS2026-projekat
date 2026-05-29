package demo.repository;

import com.influxdb.client.InfluxDBClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import demo.configuration.InfluxDBConnectionClass;
import demo.model.MatchEvent;

import java.util.List;

@Repository
public class MatchEventRepositoryImpl implements MatchEventRepository {
    @Autowired
    private final InfluxDBConnectionClass inConn;

    public MatchEventRepositoryImpl(InfluxDBConnectionClass influxDBConnectionClass) {
        this.inConn = influxDBConnectionClass;
    }

    @Override
    public Boolean save(MatchEvent matchEvent) {
        InfluxDBClient influxDBClient = inConn.buildConnection();
        Boolean isSuccess = inConn.save(influxDBClient, matchEvent);
        influxDBClient.close();
        return isSuccess;
    }

    @Override
    public List<MatchEvent> findAllByMatchId(String matchId) {
        InfluxDBClient influxDBClient = inConn.buildConnection();
        List<MatchEvent> matchEvents = inConn.findAllByMatchId(influxDBClient, matchId);
        influxDBClient.close();
        return matchEvents;
    }

    @Override
    public List<MatchEvent> findAllByPlayerId(String playerId) {
        InfluxDBClient influxDBClient = inConn.buildConnection();
        List<MatchEvent> matchEvents = inConn.findAllByPlayerId(influxDBClient, playerId);
        influxDBClient.close();
        return matchEvents;
    }

    @Override
    public List<MatchEvent> retrieveDataFromInfluxDB() {
        InfluxDBClient influxDBClient = inConn.buildConnection();
        List<MatchEvent> matchEvents = inConn.findAll(influxDBClient);
        influxDBClient.close();
        return matchEvents;
    }

    @Override
    public Boolean deleteRecord(String playerId) {
        InfluxDBClient influxDBClient = inConn.buildConnection();
        Boolean isSuccess = inConn.deleteRecord(influxDBClient, playerId);
        influxDBClient.close();
        return isSuccess;
    }
}
