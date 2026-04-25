package java.com.example.RecommendedActivitySevice.model;

import org.springframework.data.neo4j.core.schema.*;

@Node
public class Player{
    @Id
    private int playerId;

    private String name;

    private String surname;

    private String position;

    private Date dateOfBirth;

    private int height;

    private int clubId;

    private Boolean isActive;

    public Player() {
    }

    public int getPlayerId() {
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

    public int getClubId() {
        return clubId;
    }

    public Boolean getIsActive() {
        return isActive;
    }


    public void setPlayerId(int playerId) {
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

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}