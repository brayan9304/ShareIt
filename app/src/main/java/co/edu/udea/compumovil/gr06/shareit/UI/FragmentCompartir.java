package co.edu.udea.compumovil.gr06.shareit.UI;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import co.edu.udea.compumovil.gr06.shareit.R;
import co.edu.udea.compumovil.gr06.shareit.UI.daos.ProductDAO;
import co.edu.udea.compumovil.gr06.shareit.UI.model.Product;

import static android.R.attr.path;
import static android.app.Activity.RESULT_OK;


public class FragmentCompartir extends Fragment implements View.OnClickListener {
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int ACTION_CAMERA = 0;
    private Button cargarFoto;
    private EditText calification;
    private EditText productType;
    private EditText price;
    private EditText description;
    private Button share;
    private DatabaseReference myRef;
    private ProductDAO productDAO;
    private Product product;
    private ImageView productPicture;
    private Bitmap imageBitmap;
    private ByteArrayInputStream flujo;

    private FirebaseStorage storage;
    private StorageReference cubeta, carpeta;
    private String path;



    public FragmentCompartir() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_compartir, container, false);
        calification = (EditText)fragment.findViewById(R.id.eTCalification);
        productType = (EditText)fragment.findViewById(R.id.eTProductType);
        price = (EditText)fragment.findViewById(R.id.precio);
        description = (EditText)fragment.findViewById(R.id.description);
        share = (Button)fragment.findViewById(R.id.share);
        productPicture = (ImageView)fragment.findViewById(R.id.productPicture);
        cargarFoto = (Button) fragment.findViewById(R.id.boton_foto_productos);
        share.setOnClickListener(this);
        cargarFoto.setOnClickListener(this);
        path = "";
        storage = FirebaseStorage.getInstance();
        cubeta = storage.getReferenceFromUrl("gs://share-it-40aed.appspot.com");
        carpeta = cubeta.child("Imagenes Producto");
        return fragment;
    }

    public void addPhoto(View view) {
        dispatchTakePictureIntent();
    }//End addPhoto


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }//End if
    }//End dispatchTakePictureIntent

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            productPicture.setImageBitmap(imageBitmap);
            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, salida);
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
        }//End if
    }//End onActivityResult


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
                byte[] uPicture;
                ByteArrayOutputStream bitesOut = new ByteArrayOutputStream();

                if (imageBitmap == null) {
                    uPicture = null;
                } else {
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bitesOut);
                    uPicture = bitesOut.toByteArray();
                }

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                productDAO = new ProductDAO();
                product = new Product();
                product.setNameUser(currentUser.getDisplayName());
                product.setProduct_type(productType.getText().toString());
                product.setPrice(Integer.parseInt(price.getText().toString()));
                product.setDescription(description.getText().toString());
                product.setPathPoto(path);
                productDAO.addProduct(product);
                break;

            case R.id.boton_foto_productos:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }//End if
                break;
        }

    }
}
