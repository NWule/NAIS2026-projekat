package demo.saga.event;

public class MatchEventDeletionFailedEvent {
    private String sagaId;
    private Long matchId;
    private String reason;

    public MatchEventDeletionFailedEvent() {}

    public MatchEventDeletionFailedEvent(String sagaId, Long matchId, String reason) {
        this.sagaId = sagaId;
        this.matchId = matchId;
        this.reason = reason;
    }

    public String getSagaId() {
        return sagaId;
    }

    public void setSagaId(String sagaId) {
        this.sagaId = sagaId;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
