package com.uottawa.emilybonneville.seg2105_project;


import java.util.HashMap;

public class User {
    public String username, password, email, role;
    public HashMap<String, String> pendingRequests = new HashMap<>();
    public HashMap<String, String> approvedRequests = new HashMap<>();
    public HashMap<String, String> rejectedRequests = new HashMap<>();
    public User(){}
    public User(String username, String password, String email, String role){
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.pendingRequests.put("none", "none"); //initialize to nothing. key is firebase key. value is request
        this.approvedRequests.put("none", "none"); //initialize to nothing. key is firebase key. value is request
        this.rejectedRequests.put("none", "none"); //initialize to nothing. key is firebase key. value is request
    }
}
