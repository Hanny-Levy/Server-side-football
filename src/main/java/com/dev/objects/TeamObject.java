package com.dev.objects;

import javax.persistence.*;

@Entity
@Table(name = "teams")


public class TeamObject {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Id
    @Column
    private String name;

    @Column
    private int gamesWon;

    @Column
    private int gameDrawn;

    @Column
    private int gamesLost;

    @Column
    private int goalsFor;

    @Column
    private int goalAgainst;

    public TeamObject(String name) {
        this.name = name;
        this.gamesWon=0;
        this.gameDrawn=0;
        this.gamesLost=0;
        this.goalsFor=0;
        this.goalAgainst=0;
    }

    public TeamObject() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getGameDrawn() {
        return gameDrawn;
    }

    public void setGameDrawn(int gameDrawn) {
        this.gameDrawn = gameDrawn;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }

    public int getGoalAgainst() {
        return goalAgainst;
    }

    public void setGoalAgainst(int goalAgainst) {
        this.goalAgainst = goalAgainst;
    }
}
