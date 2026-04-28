package com.example.RecommendedActivityService.model;

import org.springframework.data.neo4j.core.schema.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.example.RecommendedActivityService.model.Performance;

@Node
public class Player{
    @Id
    @GeneratedValue
    private Long playerId;
    private String name;
    private String surname;
    private String position;
    private Date dateOfBirth;
    private int height;

    @Relationship(type = "OF_PLAYER", direction = Relationship.Direction.OUTGOING)
    private List<Membership> memberships;


    public Long getPlayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPosition() {
        return position;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public int getHeight() {
        return height;
    }


    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}