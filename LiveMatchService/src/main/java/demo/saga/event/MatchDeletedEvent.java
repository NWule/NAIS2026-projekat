package demo.saga.event;

import java.time.LocalDateTime;

public class MatchDeletedEvent {
    private String sagaId;
    private Long matchId;
    private LocalDateTime timestamp;

    public MatchDeletedEvent(String sagaId, Long matchId, LocalDateTime timestamp) {
        this.sagaId = sagaId;
        this.matchId = matchId;
        this.timestamp = timestamp;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
