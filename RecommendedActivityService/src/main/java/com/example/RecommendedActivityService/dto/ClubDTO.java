package com.example.RecommendedActivityService.dto;

public class ClubDTO {

    private Integer clubId;
    private String name;
    private String crest;
    private int wins;
    private int losses;
    private int draws;

    public Integer getClubId() {return clubId; }

    public void setClubId(Integer clubId) {this.clubId = clubId;}

    public String getName() {return name;}

    public String getCrest() {return crest;}

    public int getWins() {return wins;}

    public int getLosses() {return losses;}

    public int getDraws() {return draws;}

    public void setName(String name) {this.name = name;}

    public void setCrest(String crest) {this.crest = crest;}

    public void setWins(int wins) {this.wins = wins;}

    public void setLosses(int losses) {this.losses = losses;}

    public void setDraws(int draws) {this.draws = draws;}
}