package co.edu.udea.compumovil.gr06.shareit.UI.model;

/**
 * Created by brayan on 8/10/16.
 */

public class User_interest {
    private int _id;
    private int user_id;
    private int interest_id;

    public User_interest() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getInterest_id() {
        return interest_id;
    }

    public void setInterest_id(int interest_id) {
        this.interest_id = interest_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
