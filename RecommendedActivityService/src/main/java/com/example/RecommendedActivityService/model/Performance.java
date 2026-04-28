package com.example.RecommendedActivityService.model;

import org.springframework.data.neo4j.core.schema.*;

@RelationshipProperties
public class Performance {

    @Id
    @GeneratedValue
    private Long id;

    private int minutesPlayed;
    private int goals;
    private int assists;
    private int yellowCards;
    private int redCards;

    @TargetNode
    private Match match;

    public Performance() {}

    public Performance(int minutesPlayed, int goals, int assists, Match match) {
        this.minutesPlayed = minutesPlayed;
        this.goals = goals;
        this.assists = assists;
        this.match = match;
    }

    public Long getId() {
        return id;
    }

    public int getMinutesPlayed() {
        return minutesPlayed;
    }

    public void setMinutesPlayed(int minutesPlayed) {
        this.minutesPlayed = minutesPlayed;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(int yellowCards) {
        this.yellowCards = yellowCards;
    }

    public int getRedCards() {
        return redCards;
    }

    public void setRedCards(int redCards) {
        this.redCards = redCards;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}