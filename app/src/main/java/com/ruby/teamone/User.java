package com.ruby.teamone;

import java.util.ArrayList;

public class User {
    private String email;
    private ArrayList<Friend> friends;
    private String name;
    private String uid;

    public User(String email, ArrayList<Friend> friends, String name, String uid) {
        this.email = email;
        this.friends = friends;
        this.name = name;
        this.uid = uid;
    }


    public String getEmail() {
        return email;
    }

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }
}
