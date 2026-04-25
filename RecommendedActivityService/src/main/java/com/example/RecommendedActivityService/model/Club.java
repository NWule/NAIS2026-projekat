package java.com.example.RecommendedActivitySevice.model;

import org.springframework.data.neo4j.core.schema.*;

@Node
public class Club{
    @Id
    private int clubId;

    private String clubName;

    private String crest;

    private int wins;

    private int losses;

    private int draws;

    public Club() {
    }

    public Club(int clubId, String clubName, String crest, int wins, int losses, int draws) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.crest = crest;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
    }
    
    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
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