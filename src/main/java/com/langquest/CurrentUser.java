package com.langquest;

import com.langquest.model.User;

public class CurrentUser {
    private static User activeUser;
    private static boolean newlyCreated;

    public static void set(User user, boolean isNew) {
        activeUser = user;
        newlyCreated = isNew;
    }

    public static User get() {
        return activeUser;
    }

    public static boolean wasNewlyCreated() {
        return newlyCreated;
    }
}