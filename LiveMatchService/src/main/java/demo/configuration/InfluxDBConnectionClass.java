package demo.configuration;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.exceptions.InfluxException;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.influxdb.client.DeleteApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import demo.model.MatchEvent;

@Component
public class InfluxDBConnectionClass {

    @Value("${spring.influx.token}")
    private String token;

    @Value("${spring.influx.bucket}")
    private String bucket;

    @Value("${spring.influx.org}")
    private String org;

    @Value("${spring.influx.url}")
    private String url;

    public InfluxDBClient buildConnection() {
        setToken(token);
        setBucket(bucket);
        setOrg(org);
        setUrl(url);
        return InfluxDBClientFactory.create(getUrl(), getToken().toCharArray(), getOrg(), getBucket());
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getBucket() { return bucket; }
    public void setBucket(String bucket) { this.bucket = bucket; }

    public String getOrg() { return org; }
    public void setOrg(String org) { this.org = org; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public boolean save(InfluxDBClient influxDBClient, MatchEvent matchEvent) {
        boolean flag = false;
        try {
            WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
            matchEvent.setCreated(Instant.parse("2021-10-12T05:10:15.187484Z"));
            Point point = Point.measurement("match_events")
                    .addTag("player_id", matchEvent.getPlayerId())
                    .addTag("match_id", matchEvent.getMatchId())
                    .addField(matchEvent.get_field(), (double) matchEvent.get_value())
                    .time(matchEvent.getCreated(), WritePrecision.MS);

            writeApi.writePoint(point);
            flag = true;
        } catch (InfluxException e) {
        }
        return flag;
    }

    public boolean writePointbyPOJO(InfluxDBClient influxDBClient, MatchEvent matchEvent) {
        boolean flag = false;
        try {
            WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
            matchEvent.setCreated(Instant.parse("2021-10-12T05:10:15.187484Z"));
            writeApi.writeMeasurement(WritePrecision.MS, matchEvent);
            flag = true;
        } catch (InfluxException e) {
        }
        return flag;
    }

    public List<MatchEvent> findAll(InfluxDBClient influxDBClient) {
        String flux = "from(bucket:\"nais_bucket\") |> range(start:0) |> filter(fn: (r) => r[\"_measurement\"] == \"match_events\") |> sort() |> yield(name: \"sort\")";
        return getMatchEvents(influxDBClient.getQueryApi(), flux);
    }

    public List<MatchEvent> findAllByMatchId(InfluxDBClient influxDBClient, String matchId) {
        String flux = String.format(
                "from(bucket:\"nais_bucket\") |> range(start:0) |> filter(fn: (r) => r[\"_measurement\"] == \"match_events\" and r[\"match_id\"] == \"%s\") |> sort() |> yield(name: \"sort\")",
                matchId);
        return getMatchEvents(influxDBClient.getQueryApi(), flux);
    }

    public List<MatchEvent> findAllByPlayerId(InfluxDBClient influxDBClient, String playerId) {
        String flux = String.format(
                "from(bucket:\"nais_bucket\") |> range(start:0) |> filter(fn: (r) => r[\"_measurement\"] == \"match_events\" and r[\"player_id\"] == \"%s\") |> sort() |> yield(name: \"sort\")",
                playerId);
        return getMatchEvents(influxDBClient.getQueryApi(), flux);
    }

    private List<MatchEvent> getMatchEvents(QueryApi queryApi, String flux) {
        List<MatchEvent> matchEvents = new ArrayList<>();
        List<FluxTable> tables = queryApi.query(flux);
        for (FluxTable fluxTable : tables) {
            for (FluxRecord fluxRecord : fluxTable.getRecords()) {
                MatchEvent matchEvent = new MatchEvent();
                matchEvent.setMatchId((String) fluxRecord.getValueByKey("match_id"));
                matchEvent.setPlayerId((String) fluxRecord.getValueByKey("player_id"));
                matchEvent.set_field((String) fluxRecord.getValueByKey("_field"));
                matchEvent.set_value((Double) fluxRecord.getValueByKey("_value"));
                matchEvent.setCreated((Instant) fluxRecord.getValueByKey("_time"));
                matchEvents.add(matchEvent);
            }
        }
        return matchEvents;
    }

    public boolean deleteRecord(InfluxDBClient influxDBClient, String playerId) {
        boolean flag = false;
        DeleteApi deleteApi = influxDBClient.getDeleteApi();
        try {
            OffsetDateTime start = OffsetDateTime.now().minus(72, ChronoUnit.HOURS);
            OffsetDateTime stop = OffsetDateTime.now();
            String predicate = "_measurement=\"match_events\" AND player_id = \"" + playerId + "\"";
            deleteApi.delete(start, stop, predicate, bucket, org);
            flag = true;
        } catch (InfluxException ie) {
        }
        return flag;
    }
}
