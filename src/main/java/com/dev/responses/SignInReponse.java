package com.dev.responses;

import com.dev.objects.User;

public class SignInReponse extends BasicResponse{
    private User user;

    public SignInReponse(boolean success, Integer errorCode, User user) {
        super(success, errorCode);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
