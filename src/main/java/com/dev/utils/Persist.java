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
import javax.persistence.Query;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
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
           getUpdatedTeamsListWithLiveResult();
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



     public TeamObject findTeamByName(String name) {
        return (TeamObject) sessionFactory.openSession().createQuery("FROM TeamObject WHERE name =: name").setParameter("name",name).list().get(0);
    }

    public List<GameObject> getGamesByStatus(boolean isLive){
       return sessionFactory.openSession().createQuery("FROM GameObject WHERE isLive= : isLive").setParameter("isLive",isLive).list();
    }

    public List<GameObject> getAllGames(){
        return sessionFactory.openSession().createQuery("FROM GameObject ").list();
    }


//    public boolean addToGameTable(String team1 , String team2 ,int goalsForTeam1 ,int goalsForTeam2){
//        GameObject game=new GameObject(team1,team2,goalsForTeam1,goalsForTeam2,true);
//        sessionFactory.openSession().save(game);
//        return true;
//    }

    public boolean updateLiveGame(String team1 , String team2 ,int goalsForTeam1 ,int goalsForTeam2){
        Session session=sessionFactory.openSession();
        Transaction transaction=session.beginTransaction();
        GameObject currentGame =null ;
        List <GameObject> liveGames= getGamesByStatus(true);
        for (GameObject game: liveGames) {
            if((game.getTeam1().getName().equals(team1) && game.getTeam2().getName().equals(team2))||(game.getTeam1().getName().equals(team2) && game.getTeam2().getName().equals(team1))) {
                currentGame=game;
                currentGame.setTeam1GoalsFor(goalsForTeam1);
                currentGame.setTeam2GoalsFor(goalsForTeam2);
            };
        }
        if (currentGame==null )
        currentGame=new GameObject( findTeamByName(team1), findTeamByName(team2),goalsForTeam1,goalsForTeam2,true);
        session.saveOrUpdate(currentGame);
        transaction.commit();
        return true;
    }

    public boolean updateFinalGameResult(String team1 , String team2 ,int goalsForTeam1 ,int goalsForTeam2){
        Session session=sessionFactory.openSession();
        Transaction transaction=session.beginTransaction();
        GameObject game=findGameByTeamsNames(team1,team2,goalsForTeam1,goalsForTeam2);
        if (game==null){
            game=new GameObject(findTeamByName(team1),findTeamByName(team2),goalsForTeam1,goalsForTeam2,false);
        }else {
            game.setLive(false);
            game.setTeam1GoalsFor(goalsForTeam1);
            game.setTeam2GoalsFor(goalsForTeam2);
        }
        session.saveOrUpdate(game);
        transaction.commit();
        GameResult gameResult=null;
        if (goalsForTeam1==goalsForTeam2){
            updateTeamResult(team1,goalsForTeam1,goalsForTeam2,GameResult.DRAWN);
            updateTeamResult(team2,goalsForTeam2,goalsForTeam1,GameResult.DRAWN);
        } else if (goalsForTeam1 > goalsForTeam2 ){
            updateTeamResult(team1,goalsForTeam1,goalsForTeam2,GameResult.WIN);
            updateTeamResult(team2,goalsForTeam2,goalsForTeam1,GameResult.LOSE);
        }else {
            updateTeamResult(team1,goalsForTeam1,goalsForTeam2,GameResult.LOSE);
            updateTeamResult(team2,goalsForTeam2,goalsForTeam1,GameResult.WIN);
        }
        return true;
    }


    public GameObject findGameByTeamsNames(String team1 , String team2,int goalsForTeam1,int goalsForTeam2) {
        List <GameObject> games=getAllGames();
        GameObject currentGame=null;
        for (GameObject game: games) {
            if (game.getTeam1().getName().equals(team1) && (game.getTeam1GoalsFor()==goalsForTeam1) && (game.getTeam2GoalsFor()==goalsForTeam2) ){
            currentGame=game;
            break;
            }else if (game.getTeam2().getName().equals(team2) && (game.getTeam1GoalsFor()==goalsForTeam2) && (game.getTeam2GoalsFor()==goalsForTeam1)){
                currentGame=game;
                break;
            }
        }
        return currentGame;
    }

    public boolean deleteGame (String team1 ,String team2 ,int team1GoalsFor , int team2GoalsFor){
        GameObject game=findGameByTeamsNames(team1,team2,team1GoalsFor,team2GoalsFor);
        int id = game.getId();
        Session session=sessionFactory.openSession();
        session.delete(game);
        Transaction transaction = session.beginTransaction();
        transaction.commit();
        return true;
    }


    public List<TeamObject> getAllTeamsInLiveGames(){
        List<GameObject> liveGames= getGamesByStatus(true);
        List<TeamObject> teamsInGames=new ArrayList<>();
        for (GameObject game:liveGames) {
            TeamObject team1 = game.getTeam1();
            TeamObject team2 = game.getTeam2();
          if ( !(teamsInGames.contains(team1) ))
                teamsInGames.add(team1);
               if ( !(teamsInGames.contains(team2)))
                teamsInGames.add(team2);
        }
        return teamsInGames;
    }

    public HashSet<TeamObject> getUpdatedTeamsListWithLiveResult(){
        HashSet<TeamObject> updatedTeams = new HashSet<>();
        TeamObject team1=null;
        TeamObject team2=null;
        List<GameObject > liveGames=getGamesByStatus(true);
        for (GameObject game: liveGames ) {
            team1=game.getTeam1();
            team2=game.getTeam2();
            if (game.getTeam1GoalsFor()>game.getTeam2GoalsFor()){
                team1.setGamesWon(team1.getGamesWon()+1);
                team2.setGamesLost(team2.getGamesLost()+1);
            }else if (game.getTeam1GoalsFor()>game.getTeam2GoalsFor()){
                team2.setGamesWon(team2.getGamesWon()+1);
                team1.setGamesLost(team1.getGamesLost()+1);
            }else {
                team1.setGameDrawn(team1.getGameDrawn()+1);
                team2.setGameDrawn(team2.getGameDrawn()+1);
            }
            updatedTeams.add(team1);
            updatedTeams.add(team2);
        }
        for (TeamObject current: getTeams() ) {
            updatedTeams.add(current);
        }
        printSet(updatedTeams);
        return updatedTeams;
    }

    public void printSet(HashSet<TeamObject> teamObjects){
        for (TeamObject team: teamObjects  ) {
            System.out.println(team.getName());
            System.out.println(team.getGamesWon());
            System.out.println(team.getGamesLost());
            System.out.println(team.getGameDrawn());
        }
    }


}





