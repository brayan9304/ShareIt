package co.edu.udea.compumovil.gr06.shareit.UI;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import co.edu.udea.compumovil.gr06.shareit.R;
import co.edu.udea.compumovil.gr06.shareit.UI.adapter.ProductAdapter;
import co.edu.udea.compumovil.gr06.shareit.UI.model.Product;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private static DatabaseReference myRef;

    private static RecyclerView listProducts;
    private static List<Product> products;
    private static ProductAdapter productAdapter;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_serach, container, false);
        listProducts = (RecyclerView) fragment.findViewById(R.id.my_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listProducts.setLayoutManager(linearLayoutManager);
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(products);

        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        myRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mensajeRef = myRef.child(Product.CHILD);

        mensajeRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                products.add(dataSnapshot.getValue(Product.class));
                listProducts.setAdapter(productAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public static void save(String searchName) {
        List<Product> productsFind = new ArrayList<>();
        ProductAdapter productAdapterFind;

        Iterator i = products.iterator();
        while (i.hasNext()) {
            Product p = (Product) i.next();
            if (searchName.equals(p.getProductName())) {
                productsFind.add(p);
            }
        }
        productAdapterFind = new ProductAdapter(productsFind);
        listProducts.setAdapter(productAdapterFind);
    }
}
