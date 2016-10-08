package co.edu.udea.compumovil.gr06.shareit.UI.model;

/**
 * Created by brayan on 8/10/16.
 */

public class Comment_user {
    private int _id;
    private int user_id;
    private int product_id;
    private String comment;
    private int score;

    public Comment_user() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
