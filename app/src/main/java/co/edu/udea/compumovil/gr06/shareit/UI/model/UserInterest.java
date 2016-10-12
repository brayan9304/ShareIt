package co.edu.udea.compumovil.gr06.shareit.UI.model;

/**
 * Created by brayan on 8/10/16.
 */

public class UserInterest {
    //Table name
    public static String TABLE_NAME = "UserInterest";
    //Columns
    public static  String COLUMN_ID = "userInterestId";
    public static String COLUMN_INTEREST_ID = "interestId";

    private int userInterestId;
    private int interestId;

    public UserInterest() {
    }

    public int getUserInterestId() {
        return userInterestId;
    }

    public void setUserInterestId(int userInterestId) {
        this.userInterestId = userInterestId;
    }

    public int getInterestId() {
        return interestId;
    }

    public void setInterestId(int interestId) {
        this.interestId = interestId;
    }

}
