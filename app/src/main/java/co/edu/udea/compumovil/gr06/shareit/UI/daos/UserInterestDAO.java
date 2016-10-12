package co.edu.udea.compumovil.gr06.shareit.UI.daos;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.edu.udea.compumovil.gr06.shareit.UI.model.CommentUser;

/**
 * Created by brayan on 8/10/16.
 */

public class UserInterestDAO {
    private int cont = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    public void add(CommentUser commentUser){

    }
    public void edit(CommentUser commentUser){

    }
    public void delete(int idComment){

    }
}
