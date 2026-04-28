package com.example.RecommendedActivityService.model;

import org.springframework.data.neo4j.core.schema.*;

import java.time.LocalDate;
import java.util.List;

@Node
public class Membership {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate fromDate;
    private LocalDate toDate;
    private Integer jerseyNumber;

    @Relationship(type = "OF_PLAYER", direction = Relationship.Direction.INCOMING)
    private Player player;

    @Relationship(type = "OF_CLUB", direction = Relationship.Direction.OUTGOING)
    private Club club;

    @Relationship(type = "PERFORMED_IN", direction = Relationship.Direction.OUTGOING)
    private List<Performance> performances;

    public Membership() {}

    public Membership(LocalDate fromDate, LocalDate toDate, Integer jerseyNumber) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.jerseyNumber = jerseyNumber;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Integer getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(Integer jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }

    public void setId(Long id) {
        this.id = id;
    }
}