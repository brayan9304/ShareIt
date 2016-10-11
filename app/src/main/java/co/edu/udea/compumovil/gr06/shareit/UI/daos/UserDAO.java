package co.edu.udea.compumovil.gr06.shareit.UI.daos;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.edu.udea.compumovil.gr06.shareit.UI.model.User;

/**
 * Created by brayan on 8/10/16.
 */

public class UserDAO {
    private int cont = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();

    public void addUser(User user){
        cont++;
        ref.child(User.TABLE_NAME).child(Integer.toString(cont)).setValue(user);
    }



}
