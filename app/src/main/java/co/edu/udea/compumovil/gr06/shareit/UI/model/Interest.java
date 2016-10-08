package co.edu.udea.compumovil.gr06.shareit.UI.model;

/**
 * Created by brayan on 8/10/16.
 */

public class Interest {
    private int interest_id;
    private String interest_type;
    private String description;

    public Interest() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInterest_id() {
        return interest_id;
    }

    public void setInterest_id(int interest_id) {
        this.interest_id = interest_id;
    }

    public String getInterest_type() {
        return interest_type;
    }

    public void setInterest_type(String interest_type) {
        this.interest_type = interest_type;
    }
}
