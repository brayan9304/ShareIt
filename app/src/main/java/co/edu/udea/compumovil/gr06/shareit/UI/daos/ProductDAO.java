package co.edu.udea.compumovil.gr06.shareit.UI.daos;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import co.edu.udea.compumovil.gr06.shareit.UI.model.Product;
/**
 * Created by brayan on 8/10/16.
 */

public class ProductDAO {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference myRef = database.getReference();


    public ProductDAO() {

    }

    public void addProduct(Product product) {
        String key = myRef.child("products").push().getKey();
        product.setKey(key);
        DatabaseReference hijo = myRef.child("products").child(key);
        hijo.setValue(product);
    }

    public static void updateProduct(Product product) {

        String key = product.getKey();
        Map<String, Object> actualizado = new HashMap<>();
        actualizado.put("key", product.getKey());
        actualizado.put("calification", product.getCalification());
        actualizado.put("description", product.getDescription());
        actualizado.put("email", product.getEmail());
        actualizado.put("latitudPosicion", product.getLatitudPosicion());
        actualizado.put("longitudPosicion", product.getLongitudPosicion());
        actualizado.put("nameUser", product.getNameUser());
        actualizado.put("pathPoto", product.getPathPoto());
        actualizado.put("price", product.getPrice());
        actualizado.put("productName", product.getProductName());
        actualizado.put("product_type", product.getProduct_type());
        DatabaseReference hijo = myRef.child("products").child(key);
        hijo.updateChildren(actualizado);
    }
}
