package com.example.RecommendedActivityService.model;

import org.springframework.data.neo4j.core.schema.*;
import java.time.LocalDate;
import java.util.List;

@Node
public class Match {

    @Id
    private Long matchId;
    private LocalDate date;
    private String competition;
    private String stadium;

    @Relationship(type = "HAS_PERFORMANCE", direction = Relationship.Direction.INCOMING)
    private List<Performance> performances;

    public Match() {}

    public Match(Long matchId, LocalDate date, String competition, String stadium) {
        this.matchId = matchId;
        this.date = date;
        this.competition = competition;
        this.stadium = stadium;
    }

    public Long getMatchId() {
        return matchId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCompetition() {
        return competition;
    }

    public String getStadium() {
        return stadium;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }

}