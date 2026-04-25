package java.com.example.RecommendedActivitySevice.model;

import org.springframework.data.neo4j.core.schema.*;

@Node
public class Performance {

    @Id
    @GeneratedValue
    private Long id;

    private int minutesPlayed;
    private int goals;
    private int assists;
    private int yellowCards;
    private int redCards;

    // veza ka igraču
    @Relationship(type = "FOR_PLAYER", direction = Relationship.Direction.OUTGOING)
    private Player player;

    // veza ka utakmici
    @Relationship(type = "IN_MATCH", direction = Relationship.Direction.OUTGOING)
    private Match match;

    public Performance() {}

    public Performance(int minutesPlayed, int goals, int assists) {
        this.minutesPlayed = minutesPlayed;
        this.goals = goals;
        this.assists = assists;
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
