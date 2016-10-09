package co.edu.udea.compumovil.gr06.shareit.UI.model;

/**
 * Created by brayan on 8/10/16.
 */

public class User {
    //Table name
    public  static  String TABLE_NAME = "User";
    //Columns
    public static String COLUMN_ID = "user_id";
    public static String COLUMN_EMAIL = "email";

    private int user_id;
    private String email;


    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
