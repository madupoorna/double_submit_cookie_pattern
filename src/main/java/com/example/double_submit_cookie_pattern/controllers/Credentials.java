package com.example.double_submit_cookie_pattern.controllers;

import com.example.double_submit_cookie_pattern.models.UserDataModel;

import java.util.HashMap;

public class Credentials {

    private static Credentials instance = null;
    private HashMap<String, UserDataModel> userDataSet = new HashMap<>();

    private Credentials() {
    }

    public static synchronized Credentials getInstance() {
        if (instance == null) {
            instance = new Credentials();
        }
        return instance;
    }

    public String getUserName(String sessionId) {
        return userDataSet.get(sessionId).getUsername();
    }

    public String getSessionId(String sessionId){
        return userDataSet.get(sessionId).getSessionId();
    }

    public void addCredentials(UserDataModel model) {
        userDataSet.put(model.getSessionId(), model);
    }

    public void removeCredentials(String sessionId) {
        userDataSet.remove(sessionId);
    }

}
