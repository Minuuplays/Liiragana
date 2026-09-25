package com.langquest;

import com.langquest.model.User;

public class CurrentUser {
    private static User activeUser;

    public static void set(User user) {
        activeUser = user;
    }

    public static User get() {
        return activeUser;
    }
}