package co.edu.udea.compumovil.gr06.shareit.UI.model;

/**
 * Created by brayan on 8/10/16.
 */

public class CommentUser {
    //Table name
    public static String TABLE_NAME = "CommentUser";
    public static final String CHILD = "Comentarios";
    //Columns
    public static String COLUMN_ID = "commentUserId";
    public static String COLUMN_USER_ID = "userId";
    public static String COLUMN_COMMENT= "comment";
    public static String COLUMN_PRODUCT_ID = "productId";
    public static String COLUMN_SCORE = "score";


    private double score;
    private String autor;
    private String idUsuario;
    private String fecha;
    private String comentario;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public CommentUser() {
    }

}
