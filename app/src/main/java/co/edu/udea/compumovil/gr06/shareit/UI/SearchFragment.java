package co.edu.udea.compumovil.gr06.shareit.UI;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    public static int MIN_VALUE;
    public static int MAX_VALUE;

    private static DatabaseReference myRef;

    private static RecyclerView listProducts = null;
    private ProgressDialog progressDialog;
    private static List<Product> products = null;
    private static ProductAdapter productAdapter;
    private View fragment;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        progressDialog = ProgressDialog.show(getContext(), getString(R.string.messege_wait),
                getString(R.string.message_cargando), true);
        fragment = inflater.inflate(R.layout.fragment_serach, container, false);
        listProducts = (RecyclerView) fragment.findViewById(R.id.my_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listProducts.setLayoutManager(linearLayoutManager);
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(products);

        productAdapter.setOnItemClickListenerPropio(new ProductAdapter.OnItemClickListenerPropio() {
            @Override
            public void onItemClicked(View view, int position) {
                Product productSelect = products.get(position);
                Product.setProduct(productSelect);
                Intent intent = new Intent(fragment.getContext(), ViewProductCard.class);
                startActivity(intent);
            }
        });


        listProducts.setAdapter(productAdapter);

        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        myRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mensajeRef = myRef.child(Product.CHILD);
        mensajeRef.keepSynced(true);
        mensajeRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    Product nuevo = dataSnapshot.getValue(Product.class);
                    productAdapter.rellenarAdapter(nuevo);
                }
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
        progressDialog.dismiss();
    }

    @Override
    public void onPause() {
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(products);
        listProducts.setAdapter(productAdapter);
        super.onPause();
    }



    //SEARCH METHODS

    public static void search(String searchName) {
        List<Product> productsFind = new ArrayList<>();
        ProductAdapter productAdapterFind;
        if (products != null) {
            Iterator i = products.iterator();
            if (searchName.isEmpty()) {
              productsFind = products;
            } else {
                while (i.hasNext()) {
                    Product p = (Product) i.next();
                    if (p.getProductName().toLowerCase().contains(searchName.toLowerCase())) {
                        productsFind.add(p);
                    }
                }

            }

            productAdapterFind = new ProductAdapter(productsFind);
            listProducts.setAdapter(productAdapterFind);
        }
    }//End search for name

    public static void search(int min, int max, String type, float rating) {
        List<Product> productsFind = new ArrayList<>();
        ProductAdapter productAdapterFind;

        Iterator i = products.iterator();
        while (i.hasNext()) {
            Product p = (Product) i.next();
            if (p.getPrice() >= min && p.getPrice() <= max) {
                if (type.equals(p.getProduct_type())) {
                    if (rating == p.getCalification()) {
                        productsFind.add(p);
                    }
                }
            }
        }
        productAdapterFind = new ProductAdapter(productsFind);
        listProducts.setAdapter(productAdapterFind);
    }

    public static void search(String type, float rating) {
        List<Product> productsFind = new ArrayList<>();
        ProductAdapter productAdapterFind;

        Iterator i = products.iterator();
        while (i.hasNext()) {
            Product p = (Product) i.next();
            if (type.equals(p.getProduct_type())) {
                if (rating >= p.getCalification()) {
                    productsFind.add(p);
                }
            }
        }
        productAdapterFind = new ProductAdapter(productsFind);
        listProducts.setAdapter(productAdapterFind);
    }

    public static void searchType(String type) {
        List<Product> productsFind = new ArrayList<>();
        ProductAdapter productAdapterFind;

        Iterator i = products.iterator();
        while (i.hasNext()) {
            Product p = (Product) i.next();
            if (type.equals(p.getProduct_type())) {
                productsFind.add(p);
            }
        }
        productAdapterFind = new ProductAdapter(productsFind);
        listProducts.setAdapter(productAdapterFind);
    }

    public static void search(float rating) {
        List<Product> productsFind = new ArrayList<>();
        ProductAdapter productAdapterFind;

        Iterator i = products.iterator();
        while (i.hasNext()) {
            Product p = (Product) i.next();
            if (rating >= p.getCalification()) {
                productsFind.add(p);
            }
        }
        productAdapterFind = new ProductAdapter(productsFind);
        listProducts.setAdapter(productAdapterFind);
    }

    public static void search(int min, int max, String type) {
        List<Product> productsFind = new ArrayList<>();
        ProductAdapter productAdapterFind;

        Iterator i = products.iterator();
        while (i.hasNext()) {
            Product p = (Product) i.next();
            if (p.getPrice() >= min && p.getPrice() <= max) {
                if (type.equalsIgnoreCase(p.getProduct_type())) {
                    productsFind.add(p);
                }
            }
        }
        productAdapterFind = new ProductAdapter(productsFind);
        listProducts.setAdapter(productAdapterFind);
    }

    public static void search(int min, int max, float rating) {
        List<Product> productsFind = new ArrayList<>();
        ProductAdapter productAdapterFind;

        Iterator i = products.iterator();
        while (i.hasNext()) {
            Product p = (Product) i.next();
            if (p.getPrice() >= min && p.getPrice() <= max) {
                if (rating >= p.getCalification()) {
                    productsFind.add(p);
                }
            }
        }
        productAdapterFind = new ProductAdapter(productsFind);
        listProducts.setAdapter(productAdapterFind);
    }

    public static void search(int min, int max) {
        List<Product> productsFind = new ArrayList<>();
        ProductAdapter productAdapterFind;

        Iterator i = products.iterator();
        while (i.hasNext()) {
            Product p = (Product) i.next();
            if (p.getPrice() >= min && p.getPrice() <= max) {
                productsFind.add(p);
            }
        }
        productAdapterFind = new ProductAdapter(productsFind);
        listProducts.setAdapter(productAdapterFind);
    }

}
