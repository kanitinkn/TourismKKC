package com.tourismkkc;

public class DataLogin {

    private String userEmail;
    private String userPassword;

    public DataLogin(String userEmail, String userPassword) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
