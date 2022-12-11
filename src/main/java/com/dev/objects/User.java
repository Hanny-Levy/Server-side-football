package com.dev.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private int id;
    private String username;
    private String token;
    private Date creationDate;
    private List<String> notes;

    public User(String username, String token) {
        this.username = username;
        this.token = token;
        this.creationDate = new Date();
        this.notes = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void addNote (String note) {
        this.notes.add(note);
    }

    public List<String> getNotes() {
        return notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }
}
