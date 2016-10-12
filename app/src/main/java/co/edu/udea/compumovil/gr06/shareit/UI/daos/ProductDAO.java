package co.edu.udea.compumovil.gr06.shareit.UI.daos;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.edu.udea.compumovil.gr06.shareit.UI.model.Product;
/**
 * Created by brayan on 8/10/16.
 */

public class ProductDAO {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();


    public ProductDAO() {

    }

    public void addProduct(Product product){
        myRef.child("products").push().setValue(product);
    }
}
