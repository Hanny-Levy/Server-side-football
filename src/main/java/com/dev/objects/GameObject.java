package com.dev.objects;

import java.util.Date;


public class GameObject {
    private String team1;
    private String team2;


    public GameObject(String team1, String team2) {
        // Initialize the game with the names of the teams and a score of 0-0.
        this.team1 = team1;
        this.team2 = team2;

    }

    public String getTeam1() {
        return this.team1;
    }

    public String getTeam2() {
        return this.team2;
    }



}

