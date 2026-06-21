package demo.repository;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.FluxTable;
import org.springframework.stereotype.Repository;
import demo.configuration.InfluxDBConnectionClass;
import demo.model.MatchEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MatchEventRepositoryImpl implements MatchEventRepository {

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

        String flux = "from(bucket: \"nais_bucket\")\n" +
                      "  |> range(start: -30d)\n" +
                      "  |> filter(fn: (r) => r.event_type == \"goal\")\n" +
                      "  |> group(columns: [\"player_id\"])\n" +
                      "  |> count()\n" +
                      "  |> sort(columns: [\"_value\"], desc: true)\n" +
                      "  |> limit(n: 5)";

        Map<String, Long> result = new HashMap<>();
        var queryApi = influxDBClient.getQueryApi();
        var tables = queryApi.query(flux);

        for (FluxTable table : tables) {
            table.getRecords().forEach(record -> {
                String playerId = String.valueOf(record.getValueByKey("player_id"));
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
                "from(bucket: \"nais_bucket\")\n" +
                "  |> range(start: -7d)\n" +
                "  |> filter(fn: (r) => r.match_id == \"%s\" and r.event_type == \"foul\")\n" +
                "  |> group(columns: [\"club_id\"])\n" +
                "  |> count()\n" +
                "  |> sort(columns: [\"_value\"], desc: true)", matchId);

        Map<String, Long> result = new HashMap<>();
        var tables = influxDBClient.getQueryApi().query(flux);

        for (FluxTable table : tables) {
            table.getRecords().forEach(record -> {
                String clubId = String.valueOf(record.getValueByKey("club_id"));
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

        String flux = "from(bucket: \"nais_bucket\")\n" +
                      "  |> range(start: -30d)\n" +
                      "  |> filter(fn: (r) => r.event_type == \"pass\")\n" +
                      "  |> group(columns: [\"match_id\", \"player_id\"])\n" +
                      "  |> count()\n" +
                      "  |> group()\n" +
                      "  |> sort(columns: [\"_value\"], desc: true)\n" +
                      "  |> limit(n: 1)";

        Map<String, Long> result = new HashMap<>();
        var tables = influxDBClient.getQueryApi().query(flux);

        for (FluxTable table : tables) {
            table.getRecords().forEach(record -> {
                String matchId = String.valueOf(record.getValueByKey("match_id"));
                String playerId = String.valueOf(record.getValueByKey("player_id"));
                Long count = (Long) record.getValue();

                result.put(matchId + ":" + playerId, count);
            });
        }

        influxDBClient.close();
        return result;
    }

    @Override
    public Boolean markMatchAsDeleted(String matchId) {
        try {
            InfluxDBClient influxDBClient = inConn.buildConnection();

            MatchEvent tombstoneEvent = new MatchEvent(
                    matchId,
                    "SYSTEM",
                    "SYSTEM",
                    "MATCH_STATUS",
                    "is_deleted",
                    1.0,
                    java.time.Instant.now()
            );

            Boolean isSuccess = inConn.save(influxDBClient, tombstoneEvent);
            influxDBClient.close();
            return isSuccess;
        } catch (Exception e) {
            return false;
        }
    }
}