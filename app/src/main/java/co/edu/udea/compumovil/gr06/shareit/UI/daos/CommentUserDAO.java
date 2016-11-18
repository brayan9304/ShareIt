package co.edu.udea.compumovil.gr06.shareit.UI.daos;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.edu.udea.compumovil.gr06.shareit.UI.model.CommentUser;
import co.edu.udea.compumovil.gr06.shareit.UI.model.Product;

/**
 * Created by brayan on 8/10/16.
 */

public class CommentUserDAO {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();

    public void addComment(CommentUser commentUser){
        String productKey = Product.getProduct().getKey();
        DatabaseReference comentarios = ref.child(CommentUser.CHILD);
        String key = ref.child(CommentUser.CHILD).push().getKey();
        DatabaseReference hijo = comentarios.child(productKey).child(key);
        hijo.setValue(commentUser);
    }
    public void deleteComment(int commentId){
        ref.child(CommentUser.TABLE_NAME).child(Integer.toString(commentId)).removeValue();
    }
}
