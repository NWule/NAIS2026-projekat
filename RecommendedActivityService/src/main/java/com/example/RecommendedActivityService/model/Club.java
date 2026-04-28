package com.example.RecommendedActivityService.model;

import org.springframework.data.neo4j.core.schema.*;

import java.util.List;

@Node
public class Club{
    @Id
    @GeneratedValue
    private Long clubId;
    private String name;
    private String crest;
    private int wins;
    private int losses;
    private int draws;


    @Relationship(type = "PARTICIPATED_IN")
    private List<ClubMatch> matches;

    public Club() {
    }

    public Club(Long clubId, String clubName, String crest, int wins, int losses, int draws) {
        this.clubId = clubId;
        this.name = clubName;
        this.crest = crest;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
    }
    
    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public String getName() {
        return name;
    }

    public void setName(String clubName) {
        this.name = clubName;
    }

    public String getCrest() {
        return crest;
    }

    public void setCrest(String crest) {
        this.crest = crest;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

}