package com.dev.objects;
import javax.persistence.*;

@Entity
@Table(name = "games")
public class GameObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @ManyToOne
    @JoinColumn (name = "team1_name")
    private TeamObject team1;


    @ManyToOne
    @JoinColumn (name = "team2_name")
    private TeamObject team2;

    @Column
    private int team1GoalsFor;
    @Column
    private int team1Against;
    @Column
    private int team2GoalsFor;
    @Column
    private int team2Against;
    @Column
    private boolean isLive;


    public GameObject( TeamObject team1, TeamObject team2, int team1GoalsFor, int team2GoalsFor, boolean isLive) {
        this.team1 = team1;
        this.team2 = team2;
        this.team1GoalsFor = team1GoalsFor;
        this.team1Against = team2GoalsFor;
        this.team2GoalsFor = team2GoalsFor;
        this.team2Against = team1GoalsFor;
        this.isLive = isLive;
    }

    public GameObject() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getTeam2Against() {
        return team2Against;
    }

    public int getTeam1GoalsFor() {
        return team1GoalsFor;
    }

    public void setTeam1GoalsFor(int team1GoalsFor) {
        this.team1GoalsFor = team1GoalsFor;
        this.team2Against=this.team1GoalsFor ;
    }

    public int getTeam1Against() {
        return team1Against;
    }



    public int getTeam2GoalsFor() {
        return team2GoalsFor;
    }

    public void setTeam2GoalsFor(int team2GoalsFor) {
        this.team2GoalsFor = team2GoalsFor;
        this.team1Against=this.team2GoalsFor ;

    }


    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public TeamObject getTeam1() {
        return team1;
    }

    public void setTeam1(TeamObject team1) {
        this.team1 = team1;
    }

    public TeamObject getTeam2() {
        return team2;
    }

    public void setTeam2(TeamObject team2) {
        this.team2 = team2;
    }

    public void setTeam1Against(int team1Against) {
        this.team1Against = team1Against;
    }

    public void setTeam2Against(int team2Against) {
        this.team2Against = team2Against;
    }
}