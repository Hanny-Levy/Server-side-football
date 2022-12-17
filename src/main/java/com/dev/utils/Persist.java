
package com.dev.utils;

import com.dev.objects.TeamObject;
import com.dev.objects.UserObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class Persist {

    private Connection connection;

    private final SessionFactory sessionFactory;
    private UserObject user;

    @Autowired
    public Persist (SessionFactory sf) {
        this.sessionFactory = sf;
    }




    @PostConstruct
    public void createConnectionToDatabase () {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/first_project", "root", "1234");
            System.out.println("Successfully connected to DB");
            System.out.println();
            addTeams();
            addAdminUser();
            //findTeamByName("West Ham United");
            updateGameResult("Manchester United","Manchester City",1,4,6,5);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public List<UserObject> getAllUsersHibernate () {
        Session session = sessionFactory.openSession();
        List<UserObject> userObjects = session.createQuery("FROM UserObject ").list();
        session.close();
        return userObjects;
    }

//    public void addUser (String username, String token) {
//        try {
//            PreparedStatement preparedStatement =
//                    this.connection
//                            .prepareStatement("INSERT INTO users (username, token) VALUE (?,?)");
//            preparedStatement.setString(1, username);
//            preparedStatement.setString(2, token);
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    public boolean usernameAvailable (String username) {
//      boolean available = sessionFactory.openSession().createQuery("FROM UserObject WHERE  username = :username")
//              .setParameter("username", username)
//              .list().size()==0;
//        return available;
//    }
//
//    public UserObject getUserByToken (String token) {
//        UserObject user = null;
//        try {
//            PreparedStatement preparedStatement = this.connection
//                    .prepareStatement(
//                            "SELECT id, username FROM users WHERE token = ?");
//            preparedStatement.setString(1, token);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                String username = resultSet.getString("username");
//                user = new UserObject(username, token);
//                user.setId(id);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return user;
//    }



//    public String getUserByCreds (String username, String token) {
//        String response = null;
//        try {
//            PreparedStatement preparedStatement = this.connection.prepareStatement(
//                    "SELECT * FROM users WHERE username = ? AND token = ?");
//            preparedStatement.setString(1, username);
//            preparedStatement.setString(2, token);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()) {
//                response = token;
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return response;
//    }

    public void addTeams(){
       String [] teamsNames = new String[]{"AFC Bournemouth", "Arsenal", "Brentford", "Chelsea", "Everton", "Fulham", "Leeds United", "Leicester City", "Liverpool", "Manchester City", "Manchester United", "West Ham United"};

    List<TeamObject> teamObjects = sessionFactory.openSession().createQuery("FROM TeamObject ").list();
        if (teamObjects.size()==0 )
        for (int i = 0; i < teamsNames.length; i++) {
            sessionFactory.openSession().save(new TeamObject(teamsNames[i]));
        }
        }


    public void addAdminUser(){
        List <UserObject> userObjects=sessionFactory.openSession().createQuery("FROM UserObject ").list();
        if (userObjects.size()==0) {
            sessionFactory.openSession().save(new UserObject("admin","B2CAAE52C8C6A94E3D7423DB20BD083F"));
        }

    }

    public List<TeamObject> getTeams (){
        List<TeamObject> teamObjectList = sessionFactory.openSession().createQuery("FROM TeamObject ").list();
        return teamObjectList ;

    }

    public boolean updateGameResult(String team1 , String team2 ,int goalsForTeam1 ,int goalsAgainstTeam1,int goalsForTeam2,int goalsAgainstTeam2){
        TeamObject firstTeam= findTeamByName(team1);
        firstTeam.setGoalsFor(firstTeam.getGoalsFor()+goalsForTeam1);
        firstTeam.setGoalAgainst(firstTeam.getGoalAgainst()+goalsAgainstTeam1);
        sessionFactory.openSession().save(firstTeam);
        TeamObject secondTeam=findTeamByName(team2);
        secondTeam.setGoalsFor(secondTeam.getGoalsFor()+goalsForTeam2);
        secondTeam.setGoalAgainst(secondTeam.getGoalAgainst()+goalsAgainstTeam2);
        sessionFactory.openSession().save(secondTeam);
        return true;
    }

     public TeamObject findTeamByName(String name) {
        return (TeamObject) sessionFactory.openSession().createQuery("FROM TeamObject WHERE name =: name").setParameter("name",name).list().get(0);

    }

}



