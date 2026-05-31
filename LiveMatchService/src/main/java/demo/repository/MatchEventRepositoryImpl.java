package demo.repository;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.FluxTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import demo.configuration.InfluxDBConnectionClass;
import demo.model.MatchEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Map<String, Long> findTopScorers() {
        InfluxDBClient influxDBClient = inConn.buildConnection();

        String flux = "from(bucket: \"match_events\")\n" +
                "  |> range(start: -30d)\n" +
                "  |> filter(fn: (r) => r.eventType == \"goal\")\n" +
                "  |> group(columns: [\"playerId\"])\n" +
                "  |> count()\n" +
                "  |> sort(columns: [\"_value\"], desc: true)\n" +
                "  |> limit(n: 5)";

        Map<String, Long> result = new HashMap<>();
        var queryApi = influxDBClient.getQueryApi();
        var tables = queryApi.query(flux);

        for (FluxTable table : tables) {
            table.getRecords().forEach(record -> {
                String playerId = String.valueOf(record.getValueByKey("playerId"));
                Long count = (Long) record.getValue();
                result.put(playerId, count);
            });
        }

        influxDBClient.close();
        return result;
    }

    @Override
    public Map<String, Long> findTeamFoulsByMatch(String matchId) {
        InfluxDBClient influxDBClient = inConn.buildConnection();

        String flux = String.format(
                "from(bucket: \"match_events\")\n" +
                        "  |> range(start: -7d)\n" +
                        "  |> filter(fn: (r) => r.matchId == \"%s\" and r.eventType == \"foul\")\n" +
                        "  |> group(columns: [\"clubId\"])\n" +
                        "  |> count()\n" +
                        "  |> sort(columns: [\"_value\"], desc: true)", matchId);

        Map<String, Long> result = new HashMap<>();
        var tables = influxDBClient.getQueryApi().query(flux);

        for (FluxTable table : tables) {
            table.getRecords().forEach(record -> {
                String clubId = String.valueOf(record.getValueByKey("clubId"));
                Long count = (Long) record.getValue();
                result.put(clubId, count);
            });
        }

        influxDBClient.close();
        return result;
    }

    @Override
    public Map<String, Long> findMostPassesInSingleMatch() {
        InfluxDBClient influxDBClient = inConn.buildConnection();

        String flux = "from(bucket: \"match_events\")\n" +
                "  |> range(start: -30d)\n" +
                "  |> filter(fn: (r) => r.eventType == \"pass\")\n" +
                "  |> group(columns: [\"matchId\", \"playerId\"])\n" +
                "  |> count()\n" +
                "  |> group()\n" +
                "  |> sort(columns: [\"_value\"], desc: true)\n" +
                "  |> limit(n: 1)";

        Map<String, Long> result = new HashMap<>();
        var tables = influxDBClient.getQueryApi().query(flux);

        for (FluxTable table : tables) {
            table.getRecords().forEach(record -> {
                String matchId = String.valueOf(record.getValueByKey("matchId"));
                String playerId = String.valueOf(record.getValueByKey("playerId"));
                Long count = (Long) record.getValue();

                result.put(matchId + ":" + playerId, count);
            });
        }

        influxDBClient.close();
        return result;
    }
}
