package com.ruby.teamone;

public class Friend {
    private String accept;
    private String friend_email;

    public Friend(String accept, String friend_email) {
        this.accept = accept;
        this.friend_email = friend_email;
    }

    public Friend() {
    }

    public String getAccept() {
        return accept;
    }

    public String getFriend_email() {
        return friend_email;
    }

}
