
package com.dev.utils;

import com.dev.objects.GameObject;
import com.dev.objects.GameResult;
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
            System.out.println();

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

    public boolean updateTeamResult(String team ,int goalsFor ,int goalsAgainst,GameResult gameResult){
        Session session=sessionFactory.openSession();
        Transaction transaction=session.beginTransaction();
        TeamObject currentTeam= findTeamByName(team);
        currentTeam.setGoalsFor(currentTeam.getGoalsFor()+goalsFor);
        currentTeam.setGoalAgainst(currentTeam.getGoalAgainst()+goalsAgainst);
        switch(gameResult){
            case WIN:
                currentTeam.setGamesWon(currentTeam.getGamesWon()+1);
                break;
            case LOSE:
                currentTeam.setGamesLost(currentTeam.getGamesLost()+1);
                break;
            case DRAWN:
                currentTeam.setGameDrawn(currentTeam.getGameDrawn()+1);
                break;
        }
        session.saveOrUpdate(currentTeam);
        transaction.commit();
        return true;
    }

    public String findTeamNameById(int id) {
        TeamObject team = (TeamObject) sessionFactory.openSession().createQuery("FROM TeamObject WHERE id =: id").setParameter("id",id).list().get(0);
        return team.getName();
    }

     public TeamObject findTeamByName(String name) {
        return (TeamObject) sessionFactory.openSession().createQuery("FROM TeamObject WHERE name =: name").setParameter("name",name).list().get(0);
    }

    public List<GameObject> getGamesByStatus(boolean isLive){
       return sessionFactory.openSession().createQuery("FROM GameObject WHERE isLive= : isLive").setParameter("isLive",isLive).list();
    }

    public int findTeamIdByName(String teamName){
        int teamId=0;
        List<TeamObject> allTeams=getTeams();
        for (TeamObject team:allTeams) {
            if (team.getName().equals(teamName)){
               teamId= team.getId();
            }
        }
     return teamId;
    }

    public boolean addToGameTable(String team1 , String team2 ,int goalsForTeam1 ,int goalsForTeam2){
        GameObject game=new GameObject(team1,team2,goalsForTeam1,goalsForTeam2,true);
        sessionFactory.openSession().save(game);
        return true;
    }

    public boolean updateLiveGame(String team1 , String team2 ,int goalsForTeam1 ,int goalsForTeam2){
        Session session=sessionFactory.openSession();
        Transaction transaction=session.beginTransaction();
        GameObject currentGame =null ;
        List <GameObject> liveGames= getGamesByStatus(true);
        for (GameObject game: liveGames) {
            if((game.getTeam1().equals(team1) && game.getTeam2().equals(team2))||(game.getTeam1().equals(team2) && game.getTeam2().equals(team1))) {
                currentGame=game;
                currentGame.setTeam1GoalsFor(goalsForTeam1);
                currentGame.setTeam2GoalsFor(goalsForTeam2);
            };
        }
        if (currentGame==null )
        currentGame=new GameObject(team1,team2,goalsForTeam1,goalsForTeam2,true);
        session.saveOrUpdate(currentGame);
        transaction.commit();
        return true;
    }

    public boolean updateFinalGameResult(String team1 , String team2 ,int goalsForTeam1 ,int goalsForTeam2){
        Session session=sessionFactory.openSession();
        Transaction transaction=session.beginTransaction();
        GameObject game=new GameObject(team1,team2,goalsForTeam1,goalsForTeam2,false);
        session.saveOrUpdate(game);
        transaction.commit();
        GameResult gameResult=null;
        if (goalsForTeam1==goalsForTeam2){
            gameResult=GameResult.DRAWN;
        } else
            gameResult=GameResult.WIN;

         if (goalsForTeam1 > goalsForTeam2 ){
            updateTeamResult(team1,goalsForTeam1,goalsForTeam2,gameResult);
            updateTeamResult(team2,goalsForTeam2,goalsForTeam1,GameResult.LOSE);
        }else{
            updateTeamResult(team1,goalsForTeam1,goalsForTeam2,GameResult.LOSE);
            updateTeamResult(team2,goalsForTeam2,goalsForTeam1,gameResult);
        }
        return true;
    }


//    public GameObject findLiveGameByTeamsName(String team1 , String team2) {
//        return (GameObject) sessionFactory.openSession().createQuery("FROM GameObject WHERE ((team1 =: team1 AND team2 =: team2) OR (team1 =: team2 AND team2 =: team1 ) AND (isLive := true ))").setParameter("name",name).list().get(0);
//    }
}





