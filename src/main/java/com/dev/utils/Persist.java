
package com.dev.utils;

import com.dev.objects.TeamObject;
import com.dev.objects.UserObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.persistence.Query;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Component
public class Persist {

    private Connection connection;

    private final SessionFactory sessionFactory;
    private UserObject user;

    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }


    @PostConstruct
    public void createConnectionToDatabase() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/first_project", "root", "1234");
            System.out.println("Successfully connected to DB");
            System.out.println();
            addTeams();
            updateGameResult("Manchester United","Manchester City",1,4,6,5);
            addAdminUser();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean usernameAvailable (String username) {
        boolean available;
        List<UserObject> users = sessionFactory.openSession().createQuery("FROM UserObject WHERE username=:username").setParameter("username",username).list();
        available= users.size() == 0;
        return available;
    }
    public UserObject getUserByToken (String token) {
        UserObject userObject=null;
        List <UserObject> userObjects =  sessionFactory.openSession().createQuery("FROM  UserObject where token=:token").setParameter("token",token).list();
        if (userObjects.size()!=0)
            userObject=userObjects.get(0);

        return userObject;

    }


    public void addTeams() {
        String[] teamsNames = new String[]{"AFC Bournemouth", "Arsenal", "Brentford", "Chelsea", "Everton", "Fulham", "Leeds United", "Leicester City", "Liverpool", "Manchester City", "Manchester United", "West Ham United"};

        List<TeamObject> teamObjects = sessionFactory.openSession().createQuery("FROM TeamObject ").list();
        if (teamObjects.size() == 0)
            for (int i = 0; i < teamsNames.length; i++) {
                sessionFactory.openSession().save(new TeamObject(teamsNames[i]));
            }
    }


    public void addAdminUser() {
        List<UserObject> userObjects = sessionFactory.openSession().createQuery("FROM UserObject ").list();
        if (userObjects.size() == 0) {
            sessionFactory.openSession().save(new UserObject("admin@", "F842CBD9D7D0B3F80F455498B229671F"));
        }

    }

    public List<TeamObject> getTeams() {
        List<TeamObject> teamObjectList = sessionFactory.openSession().createQuery("FROM TeamObject ").list();
        return teamObjectList;

    }

    public boolean updateGameResult(String team1 , String team2 ,int goalsForTeam1 ,int goalsAgainstTeam1,int goalsForTeam2,int goalsAgainstTeam2){
       Session session=sessionFactory.openSession();
        Transaction transaction=session.beginTransaction();
        TeamObject firstTeam= findTeamByName(team1);
        firstTeam.setGoalsFor(firstTeam.getGoalsFor()+goalsForTeam1);
        firstTeam.setGoalAgainst(firstTeam.getGoalAgainst()+goalsAgainstTeam1);
        session.saveOrUpdate(firstTeam);
        TeamObject secondTeam=findTeamByName(team2);
        secondTeam.setGoalsFor(secondTeam.getGoalsFor()+goalsForTeam2);
        secondTeam.setGoalAgainst(secondTeam.getGoalAgainst()+goalsAgainstTeam2);
        session.saveOrUpdate(secondTeam);
        transaction.commit();
        return true;
    }

     public TeamObject findTeamByName(String name) {
        return (TeamObject) sessionFactory.openSession().createQuery("FROM TeamObject WHERE name =: name").setParameter("name",name).list().get(0);

    }




}



