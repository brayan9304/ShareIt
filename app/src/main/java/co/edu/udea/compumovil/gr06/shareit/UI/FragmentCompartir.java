package co.edu.udea.compumovil.gr06.shareit.UI;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import co.edu.udea.compumovil.gr06.shareit.R;
import co.edu.udea.compumovil.gr06.shareit.UI.daos.ProductDAO;
import co.edu.udea.compumovil.gr06.shareit.UI.model.Product;


public class FragmentCompartir extends Fragment implements View.OnClickListener {
    private Spinner calification;
    private Spinner productType;
    private EditText price;
    private EditText description;
    private EditText productName;
    private Button share, cargarFoto;
    private DatabaseReference myRef;
    private ProductDAO productDAO;
    private Product product;
    private ByteArrayInputStream flujo;
    private View fragment;

    private static final int ACTION_CAMERA = 0;

    private FirebaseStorage storage;
    private StorageReference cubeta, carpeta;
    private String path;


    public FragmentCompartir() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_compartir, container, false);
        //calification = (Spinner)fragment.findViewById(R.id.calification);
        //productType = (Spinner)fragment.findViewById(R.id.productType);
        price = (EditText)fragment.findViewById(R.id.precio);
        description = (EditText)fragment.findViewById(R.id.description);
        share = (Button)fragment.findViewById(R.id.share);
        cargarFoto = (Button) fragment.findViewById(R.id.boton_foto_productos);
        productName = (EditText)fragment.findViewById(R.id.EditText_productName);
        price = (EditText) fragment.findViewById(R.id.precio);
        description = (EditText) fragment.findViewById(R.id.description);
        share.setOnClickListener(this);
        cargarFoto.setOnClickListener(this);

        path = "";
        storage = FirebaseStorage.getInstance();
        cubeta = storage.getReferenceFromUrl("gs://share-it-40aed.appspot.com");
        carpeta = cubeta.child("Imagenes Producto");

        return fragment;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                productDAO = new ProductDAO();
                product = new Product();
                product.setNameUser(currentUser.getDisplayName());
                product.setProductName(productName.getText().toString());
                product.setPrice(Integer.parseInt(price.getText().toString()));
                product.setDescription(description.getText().toString());
                product.setPathPoto(path);
                path = "";
                productDAO.addProduct(product);
                break;
            case R.id.boton_foto_productos:
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, ACTION_CAMERA);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == ACTION_CAMERA) {
            if (data != null) {
                Bitmap imagen = (Bitmap) data.getExtras().get("data");
                ImageView containerFoto = (ImageView) fragment.findViewById(R.id.productPicture);
                containerFoto.setImageBitmap(imagen);

                ByteArrayOutputStream salida = new ByteArrayOutputStream();
                imagen.compress(Bitmap.CompressFormat.PNG, 0, salida);
                byte[] datos = salida.toByteArray();
                flujo = new ByteArrayInputStream(datos);
                Random t = new Random();
                int valor = t.nextInt();
                StorageReference archivo = carpeta.child(valor + ".png");
                UploadTask uploadTask = archivo.putStream(flujo);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        path = downloadUrl.toString();
                    }
                });
            }
        }
    }

    public void cargarFoto() {

    }
}
