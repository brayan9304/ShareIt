package co.edu.udea.compumovil.gr06.shareit.UI.model;

/**
 * Created by brayan on 8/10/16.
 */

public class CommentUser {
    //Table name
    public static String TABLE_NAME = "CommentUser";
    //Columns
    public static String COLUMN_ID = "commentUserId";
    public static String COLUMN_USER_ID = "userId";
    public static String COLUMN_COMMENT= "comment";
    public static String COLUMN_PRODUCT_ID = "productId";
    public static String COLUMN_SCORE = "score";


    private int commentUSerId;
    private int userId;
    private int productId;
    private String comment;
    private int score;

    public CommentUser() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCommentUSerId() {
        return commentUSerId;
    }

    public void setCommentUSerId(int commentUSerId) {
        this.commentUSerId = commentUSerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
