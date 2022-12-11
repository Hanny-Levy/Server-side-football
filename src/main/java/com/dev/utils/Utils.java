package com.dev.utils;

import org.springframework.stereotype.Component;

@Component
public class Utils {

    public boolean validateUsername (String username) {
        boolean valid = false;
        if (username != null) {
            if (username.contains("@")) {
                valid = true;
            }
        }
        return valid;
    }

    public boolean validatePassword (String password) {
        boolean valid = false;
        if (password != null) {
            if (password.length() >= 8) {
                valid = true;
            }
        }
        return valid;
    }

    public boolean validateNote (String note) {
        boolean valid = false;
        if (note != null && note.length() > 0) {
            valid = true;
        }
        return valid;
    }
}
