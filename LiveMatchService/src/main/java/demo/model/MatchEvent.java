package demo.model;

import java.time.Instant;

public class MatchEvent {
    private String matchId;
    private String playerId;
    private String clubId;
    private String eventType;

    private String _field;
    private Double _value;

    private Instant created;

    public MatchEvent() {}

    public MatchEvent(String matchId, String playerId, String clubId, String tipDogadjaja, String _field, Double _value, Instant created) {
        this.matchId = matchId;
        this.playerId = playerId;
        this.clubId = clubId;
        this.eventType = tipDogadjaja;
        this._field = _field;
        this._value = _value;
        this.created = created;
    }

    public String getMatchId() { return matchId; }
    public void setMatchId(String matchId) { this.matchId = matchId; }

    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }

    public String getClubId() { return clubId; }
    public void setClubId(String clubId) { this.clubId = clubId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String get_field() { return _field; }
    public void set_field(String _field) { this._field = _field; }

    public Double get_value() { return _value; }
    public void set_value(Double _value) { this._value = _value; }

    public Instant getCreated() { return created; }
    public void setCreated(Instant created) { this.created = created; }
}
