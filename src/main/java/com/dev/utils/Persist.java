
package com.dev.utils;

import com.dev.objects.TeamObject;
import com.dev.objects.UserObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.List;

@Component
public class Persist {

    private Connection connection;
    private final SessionFactory sessionFactory;


    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }


    @PostConstruct
    public void createConnectionToDatabase() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/football_project", "root", "1234");
            System.out.println("Successfully connected to DB");
            System.out.println();
            addTeams();
            addAdminUser();
            updateGameResult("Manchester United","Manchester City",1,4,6,5);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean usernameExist(String username) {
        boolean exist;
      List users = sessionFactory.openSession().createQuery("FROM UserObject WHERE username=: username").setParameter("username",username).list();
        exist=users.size() != 0;
        return exist;
    }
    public UserObject isUserExist (String username,String token) {
        UserObject user = null;
        List usersByName = sessionFactory.openSession().createQuery("FROM UserObject WHERE username=:username").setParameter("username",username).list();
        List userByToken= sessionFactory.openSession().createQuery("FROM UserObject WHERE token=: token").setParameter("token",token).list();
       if (usersByName.size()!=0 && userByToken.size()!=0){
           user= (UserObject) usersByName.get(0);
       }
        
        return user;

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
        String username="admin@";
        String token=createHash(username,"123456")    ;
        List<UserObject> userObjects = sessionFactory.openSession().createQuery("FROM UserObject ").list();
        if (userObjects.size() == 0) {
            sessionFactory.openSession().save(new UserObject(username,token));
            System.out.println("\n"+"successfully added"+"\n");
        }

    }
    public String createHash(String username, String password) {
        String raw = String.format("%s_%s", username, password);
        String myHash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(raw.getBytes());
            byte[] digest = md.digest();
            myHash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return myHash;
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



