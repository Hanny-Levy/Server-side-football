package com.dev.responses;

import com.dev.objects.UserObject;

public class SignInReponse extends BasicResponse{
    private UserObject user;

    public SignInReponse(boolean success, Integer errorCode, UserObject user) {
        super(success, errorCode);
        this.user = user;
    }

    public UserObject getUser() {
        return user;
    }

    public void setUser(UserObject user) {
        this.user = user;
    }
}
