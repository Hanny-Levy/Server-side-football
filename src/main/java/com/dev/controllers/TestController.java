package com.dev.controllers;

import com.dev.objects.TeamObject;

import com.dev.utils.Persist;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;


@RestController
public class TestController {


    @Autowired
    public Utils utils;


    @Autowired
    private Persist persist;

    @PostConstruct
    public void init () {
    }


    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String getCheck () {
        return "Success from get request";
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public String postCheck () {
        return "Success from post request";
    }


//    @RequestMapping(value = "/get-all-users", method = {RequestMethod.GET, RequestMethod.POST})
//    public List<User> getAllUsers () {
//        List<User> allUsers = persist.getAllUsers();
//        return allUsers;
//    }


    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public Object test () {
        return new Date().toString();
    }



//    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
//    public BasicResponse signIn (String username, String password) {
//        BasicResponse basicResponse = null;
//        String token = createHash(username, password);
//        token = persist.getUserByCreds(username, token);
//        if (token == null) {
//            if (persist.usernameAvailable(username)) {
//                basicResponse = new BasicResponse(false, 1);
//            } else {
//                basicResponse = new BasicResponse(false, 2);
//            }
//        } else {
//            User user = persist.getUserByToken(token);
//            basicResponse = new SignInReponse(true, null, user);
//        }
//        return basicResponse;
//    }

//    @RequestMapping(value = "/create-account", method = {RequestMethod.GET, RequestMethod.POST})
//    public User createAccount (String username, String password) {
//        User newAccount = null;
//        if (utils.validateUsername(username)) {
//            if (utils.validatePassword(password)) {
//                if (persist.usernameAvailable(username)) {
//                    String token = createHash(username, password);
//                    newAccount = new User(username, token);
//                    persist.addUser(username, token);
//                } else {
//                    System.out.println("username already exits");
//                }
//            } else {
//                System.out.println("password is invalid");
//            }
//        } else {
//            System.out.println("username is invalid");
//        }
//        return newAccount;
//    }


    @RequestMapping(value = "/getToken", method = {RequestMethod.GET})
    public String createHash (String username, String password) {
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

    @RequestMapping(value = "/getAllTeams", method = {RequestMethod.GET, RequestMethod.POST})
    public List<TeamObject> getAllTeams (){
       return persist.getTeams();
    }

//    private boolean checkIfUsernameExists (String username) {
//        boolean exists = false;
//        for (User user : this.myUsers) {
//            if (user.getUsername().equals(username)) {
//                exists = true;
//                break;
//            }
//        }
//
//        return exists;
//    }






//    private User getUserByToken (String token) {
//        User matchedUser = null;
//        if (token != null) {
//            for (User user : this.myUsers) {
//                if (user.getToken().equals(token)) {
//                    matchedUser = user;
//                    break;
//                }
//            }
//        }
//        return matchedUser;
//    }




}
