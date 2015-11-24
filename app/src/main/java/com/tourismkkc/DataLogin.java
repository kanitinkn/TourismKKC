package com.tourismkkc;

/**
 * Created by Amnart on 24/11/2558.
 */
public class DataLogin {
    private String user_email;
    private String user_password;

    public DataLogin(String user_email,String user_password){
        this.user_email = user_email;
        this.user_password = user_password;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_password() {
        return user_password;
    }


}
