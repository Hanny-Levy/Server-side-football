package com.dev.controllers;

import com.dev.objects.TeamObject;

import com.dev.objects.UserObject;
import com.dev.responses.BasicResponse;
import com.dev.responses.SignInReponse;
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
    public void init() {
    }


    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String getCheck() {
        return "Success from get request";
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public String postCheck() {
        return "Success from post request";
    }


//    @RequestMapping(value = "/get-all-users", method = {RequestMethod.GET, RequestMethod.POST})
//    public List<User> getAllUsers () {
//        List<User> allUsers = persist.getAllUsers();
//        return allUsers;
//    }


    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public Object test() {
        return new Date().toString();
    }


    @RequestMapping(value = "/sign-in", method = {RequestMethod.POST, RequestMethod.GET})
    public BasicResponse signIn(String username, String password) {
        BasicResponse basicResponse;
        String token ="13B151B1C4898434779FD69404F58CF3";
        System.out.println(token);
        UserObject user = persist.getUserByToken(token);
     //   System.out.println(user);
        if (user==null) {
            if (persist.usernameAvailable(username)) {
                //for problem with usename
                basicResponse = new BasicResponse(false, 1);
            } else {
                //problem with password
                basicResponse = new BasicResponse(false, 2);
            }
        } else {

            basicResponse = new SignInReponse(true, null, user);
        }
        return basicResponse;
    }


    @RequestMapping(value = "/getToken", method = {RequestMethod.GET})
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

    @RequestMapping(value = "/getAllTeams", method = {RequestMethod.GET, RequestMethod.POST})
    public List<TeamObject> getAllTeams() {
        return persist.getTeams();

    }
}


