package com.tourismkkc;

public class DataRegister {

    private String userEmail = "";
    private String userPassword = "";
    private String userFirstName = "";
    private String userLastName = "";
    private String user_fb_id = "";

    //  normal register
    public DataRegister(String userEmail, String userPassword, String userFirstName, String userLastName) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
    }

    // facebook register
    public DataRegister(String userEmail, String userPassword, String userFirstName, String userLastName, String user_fb_id) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.user_fb_id = user_fb_id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public String getUser_fb_id() {
        return user_fb_id;
    }
}
