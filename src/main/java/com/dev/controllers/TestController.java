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



//    @RequestMapping(value = "/get-all-users", method = {RequestMethod.GET, RequestMethod.POST})
//    public List<User> getAllUsers () {
//        List<User> allUsers = persist.getAllUsers();
//        return allUsers;
//    }


    @RequestMapping(value = "/sign-in", method = {RequestMethod.POST, RequestMethod.GET})
    public BasicResponse signIn(String username, String password) {
        BasicResponse basicResponse=null;
        String token = persist.createHash(username,password);
         UserObject user = persist.isUserExist(username,token);
           if (user==null) {
            if (!persist.usernameExist(username)) {
                //for problem with username
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



    @RequestMapping(value = "/getAllTeams", method = {RequestMethod.GET, RequestMethod.POST})
    public List<TeamObject> getAllTeams() {
        return persist.getTeams();

    }

}


