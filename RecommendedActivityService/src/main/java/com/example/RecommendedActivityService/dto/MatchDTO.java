package com.example.RecommendedActivityService.dto;

import java.time.LocalDate;

public class MatchDTO {

    private Integer matchId;
    private LocalDate date;
    private String competition;
    private String stadium;

    public Integer getMatchId() {
        return matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
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

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }
}