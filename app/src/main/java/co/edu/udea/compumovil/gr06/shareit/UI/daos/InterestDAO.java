package co.edu.udea.compumovil.gr06.shareit.UI.daos;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by brayan on 8/10/16.
 */

public class InterestDAO {
    private int cont = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
}
