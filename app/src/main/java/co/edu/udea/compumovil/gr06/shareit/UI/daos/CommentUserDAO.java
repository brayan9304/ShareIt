package co.edu.udea.compumovil.gr06.shareit.UI.daos;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.edu.udea.compumovil.gr06.shareit.UI.model.CommentUser;

/**
 * Created by brayan on 8/10/16.
 */

public class CommentUserDAO {
    private int cont = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();

    public void addComment(CommentUser commentUser){
        cont++;
        ref.child(CommentUser.TABLE_NAME).child(Integer.toString(cont)).setValue(commentUser);
    }
    public void deleteComment(int commentId){
        ref.child(CommentUser.TABLE_NAME).child(Integer.toString(commentId)).removeValue();
    }
}
