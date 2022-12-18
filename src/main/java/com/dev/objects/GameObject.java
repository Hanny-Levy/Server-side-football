package com.dev.objects;

import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "games")

public class GameObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;



    @ManyToOne
    @JoinColumn(name = "teamId")
    @Column
    private TeamObject team2;



    @ManyToMany
    @JoinTable(
            name = "GAME_TABLE",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    Set<TeamObject> likedCourses;





    @Column
    private boolean isLive;

    public GameObject(int id, TeamObject team1, TeamObject team2, boolean isLive) {
        this.id = id;
        this.team2 = team2;
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



    public TeamObject getTeam2() {
        return team2;
    }

    public void setTeam2(TeamObject team2) {
        this.team2 = team2;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }
}