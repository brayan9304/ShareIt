package co.edu.udea.compumovil.gr06.shareit.UI.model;

/**
 * Created by brayan on 8/10/16.
 */

public class Interest {
    //Table name
    public static String TABLE_NAME = "Interest";
    //Columns
    public static String COLUMN_ID = "interestId";
    public static String TCOLUMN_INTEREST_TYPE = "interestType";
    public static String COLUMN_DESCRIPTION = "description";

    private int interestId;
    private String interestType;
    private String description;

    public Interest() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInterestId() {
        return interestId;
    }

    public void setInterestId(int interestId) {
        this.interestId = interestId;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }
}
