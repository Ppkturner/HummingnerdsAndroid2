package com.example.lazarus.app;

import android.app.Application;

/**
 * Created by Scott on 4/24/2015.
 */
public class GlobalVariable {
    private String uid;
    private String gid;

    public String getUsername() {
        return uid;
    }

    public String getGroup() {
        return(gid);
    }

    public void setUsername(String username) {
        this.uid = username;
    }

    public void setGroup(String group) {
        this.uid = group;
    }
}
