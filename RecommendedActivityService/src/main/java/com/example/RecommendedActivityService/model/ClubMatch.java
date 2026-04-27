package com.example.RecommendedActivityService.model;

import org.springframework.data.neo4j.core.schema.*;



@RelationshipProperties
public class ClubMatch {

    @Id
    @GeneratedValue
    private Long id;

    private MatchRole role;
    private int goalsFor;
    private int goalsAgainst;
    private int shots;
    private int possession;

    @TargetNode
    private Match match;

    public ClubMatch() {}

    public ClubMatch(MatchRole role, Match match) {
        this.role = role;
        this.match = match;
    }

    public Long getId() {
        return id;
    }

    public MatchRole getRole() {
        return role;
    }

    public void setRole(MatchRole role) {
        this.role = role;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public int getShots() {
        return shots;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    public int getPossession() {
        return possession;
    }

    public void setPossession(int possession) {
        this.possession = possession;
    }
}