package com.dev.objects;
import javax.persistence.*;

@Entity
@Table(name = "games")
public class GameObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;


    @JoinColumn (name = "team_id_1")

    private int team1;



    @JoinColumn (name = "team_id_2")

    private int team2;
//
//    @ManyToOne
//    @JoinColumn(name = "teamId")
//    @Column
//    private TeamObject team1;

//    @OneToMany
//    @
//    @Column
//    private TeamObject team2;

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


    public GameObject( int team1, int team2, int team1GoalsFor, int team1Against, int team2GoalsFor, int team2Against, boolean isLive) {
        this.team1 = team1;
        this.team2 = team2;
        this.team1GoalsFor = team1GoalsFor;
        this.team1Against = team1Against;
        this.team2GoalsFor = team2GoalsFor;
        this.team2Against = team2Against;
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

    public int getTeam1() {
        return team1;
    }

    public void setTeam1(int team1) {
        this.team1 = team1;
    }

    public int getTeam2() {
        return team2;
    }

    public void setTeam2(int team2) {
        this.team2 = team2;
    }

    public int getTeam1GoalsFor() {
        return team1GoalsFor;
    }

    public void setTeam1GoalsFor(int team1GoalsFor) {
        this.team1GoalsFor = team1GoalsFor;
    }

    public int getTeam1Against() {
        return team1Against;
    }

    public void setTeam1Against(int team1Against) {
        this.team1Against = team1Against;
    }

    public int getTeam2GoalsFor() {
        return team2GoalsFor;
    }

    public void setTeam2GoalsFor(int team2GoalsFor) {
        this.team2GoalsFor = team2GoalsFor;
    }

    public int getTeam2Against() {
        return team2Against;
    }

    public void setTeam2Against(int team2Against) {
        this.team2Against = team2Against;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }
}