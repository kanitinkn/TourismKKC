package com.tourismkkc;

/**
 * Created by Amnart on 24/11/2558.
 */
public class DataRegister {
    private String user_email;
    private String user_password;
    private String user_fname;
    private String user_lname;

    public DataRegister(String user_email, String user_password, String user_fname, String user_lname) {
        this.user_email = user_email;
        this.user_password = user_password;
        this.user_fname = user_fname;
        this.user_lname = user_lname;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public String getUser_fname() {
        return user_fname;
    }

    public String getUser_lname() {
        return user_lname;
    }


}
