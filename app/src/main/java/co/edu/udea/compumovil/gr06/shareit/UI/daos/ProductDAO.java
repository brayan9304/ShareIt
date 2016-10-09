package co.edu.udea.compumovil.gr06.shareit.UI.daos;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import co.edu.udea.compumovil.gr06.shareit.UI.model.Product;
/**
 * Created by brayan on 8/10/16.
 */

public class ProductDAO {
    private int cont = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public void addProduct(Product product){
        cont++;
        ref.child(Product.TABLE_NAME).child(Integer.toString(cont)).setValue(product);
    }

    public void deleteProduct(int productId){
        ref.child(Product.TABLE_NAME).child(Integer.toString(productId)).removeValue();
    }
}
