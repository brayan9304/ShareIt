package co.edu.udea.compumovil.gr06.shareit.UI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import co.edu.udea.compumovil.gr06.shareit.R;
import co.edu.udea.compumovil.gr06.shareit.UI.daos.ProductDAO;
import co.edu.udea.compumovil.gr06.shareit.UI.model.Product;


public class FragmentCompartir extends Fragment implements View.OnClickListener {
    private Spinner calification;
    private Spinner productType;
    private EditText price;
    private EditText description;
    private EditText productName;
    private Button share;
    private DatabaseReference myRef;
    private ProductDAO productDAO;
    private Product product;


    public FragmentCompartir() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_compartir, container, false);
<<<<<<< HEAD
        calification = (Spinner)fragment.findViewById(R.id.calification);
        productType = (Spinner)fragment.findViewById(R.id.productType);
        price = (EditText)fragment.findViewById(R.id.precio);
        description = (EditText)fragment.findViewById(R.id.description);
        share = (Button)fragment.findViewById(R.id.share);
        productName = (EditText)fragment.findViewById(R.id.EditText_productName);
=======
        calification = (Spinner) fragment.findViewById(R.id.calification);
        productType = (Spinner) fragment.findViewById(R.id.productType);
        price = (EditText) fragment.findViewById(R.id.precio);
        description = (EditText) fragment.findViewById(R.id.description);
        share = (Button) fragment.findViewById(R.id.share);
>>>>>>> c48efdfac23810dd4733836d8e98e27837ffac86
        share.setOnClickListener(this);
        return fragment;
    }


    @Override
    public void onClick(View v) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        productDAO = new ProductDAO();
        product = new Product();
        product.setNameUser(currentUser.getDisplayName());
        product.setProductName(productName.getText().toString());
        product.setPrice(Integer.parseInt(price.getText().toString()));
        product.setDescription(description.getText().toString());
        productDAO.addProduct(product);
    }
}
