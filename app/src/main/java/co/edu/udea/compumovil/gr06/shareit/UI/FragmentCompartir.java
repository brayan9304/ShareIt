package co.edu.udea.compumovil.gr06.shareit.UI;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import static android.R.attr.gestureColor;
import static android.R.attr.path;
import static android.app.Activity.RESULT_OK;


public class FragmentCompartir extends Fragment implements View.OnClickListener {

    static final String STATE_PHOTO= "imageBitmap";
    static final String STATE_NAME= "product_name";
    static final String STATE_TYPE= "productType";
    static final String STATE_PRICE = "price";
    static final String STATE_DESCRIPTION = "description";
    static final String STATE_RATING = "ratingBar";
    static final String STATE_PATH="path";



    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int ACTION_CAMERA = 0;
    private static RatingBar ratingBar;
    private static FloatingActionButton cargarFoto;
    private static Spinner productType;
    private static EditText price;
    private static EditText description;
    private static DatabaseReference myRef;
    private static ProductDAO productDAO;
    private static Product product;
    private static ImageView productPicture;
    private static Bitmap imageBitmap;
    private static EditText product_name;
    private static ByteArrayInputStream flujo;



    private FirebaseStorage storage;
    private StorageReference cubeta, carpeta;
    private static String path;



    public FragmentCompartir() {
        // Required empty public constructor
    }



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putParcelable(STATE_PHOTO, imageBitmap);
        savedInstanceState.putString(STATE_NAME, product_name.getText().toString());
        savedInstanceState.putString(STATE_DESCRIPTION, description.getText().toString());
        savedInstanceState.putString(STATE_PRICE, price.getText().toString());
        savedInstanceState.putInt(STATE_TYPE, productType.getSelectedItemPosition());
        savedInstanceState.putFloat(STATE_RATING, ratingBar.getRating());
        savedInstanceState.putString(STATE_PATH,path);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View fragment = inflater.inflate(R.layout.fragment_compartir, container, false);
        productType = (Spinner) fragment.findViewById(R.id.eTProductType);
        price = (EditText) fragment.findViewById(R.id.precio);
        description = (EditText) fragment.findViewById(R.id.description);
        product_name = (EditText) fragment.findViewById(R.id.nameProduct);
        productPicture = (ImageView) fragment.findViewById(R.id.productPicture);
        ratingBar = (RatingBar) fragment.findViewById(R.id.rating);
        cargarFoto = (FloatingActionButton) fragment.findViewById(R.id.boton_foto_productos);
        cargarFoto.setOnClickListener(this);
        path = "";
        storage = FirebaseStorage.getInstance();
        cubeta = storage.getReferenceFromUrl("gs://share-it-40aed.appspot.com");
        carpeta = cubeta.child("Imagenes Producto");

        if(savedInstanceState != null){
            ratingBar.setRating(savedInstanceState.getFloat(STATE_RATING));
            price.setText(savedInstanceState.getString(STATE_PRICE));
            description.setText(savedInstanceState.getString(STATE_DESCRIPTION));
            product_name.setText(savedInstanceState.getString(STATE_NAME));
            imageBitmap = savedInstanceState.getParcelable(STATE_PHOTO);
            productType.setSelection(savedInstanceState.getInt(STATE_TYPE));
            path = savedInstanceState.getString(STATE_PATH);
            if(imageBitmap == null){
                productPicture.setImageResource(R.drawable.ic_insert_photo_black_48dp);
            }else {
                productPicture.setImageBitmap(imageBitmap);
            }

        }

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

            case R.id.boton_foto_productos:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }//End if
                break;
        }

    }

    public static void share(Context context) {
        byte[] uPicture;
        ByteArrayOutputStream bitesOut = new ByteArrayOutputStream();

        if (imageBitmap == null) {
            uPicture = null;
        } else {
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bitesOut);
            uPicture = bitesOut.toByteArray();
        }

        if (path.isEmpty()) {
            Toast.makeText(context, "La foto es requerida", Toast.LENGTH_LONG).show();
        } else if (product_name.getText().toString().isEmpty()) {
            Toast.makeText(context, "El nombre del producto es requerido", Toast.LENGTH_LONG).show();
        } else if(ratingBar.getRating() == 0){
            Toast.makeText(context, "La calificacion es requerida", Toast.LENGTH_LONG).show();
        } else if (price.getText().toString().isEmpty()) {
            Toast.makeText(context, "El precio del producto es requerida", Toast.LENGTH_LONG).show();
        } else if (description.getText().toString().isEmpty()) {
            Toast.makeText(context, "Tu opinion personal es requerido", Toast.LENGTH_LONG).show();
        } else {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            productDAO = new ProductDAO();
            product = new Product();
            product.setNameUser(currentUser.getDisplayName());
            product.setProduct_type(productType.getSelectedItem().toString());
            product.setPrice(Integer.parseInt(price.getText().toString()));
            product.setDescription(description.getText().toString());
            product.setProductName(product_name.getText().toString());
            product.setPathPoto(path);
            product.setCalification(ratingBar.getRating());
            productDAO.addProduct(product);
            Toast.makeText(context, "Producto guardado", Toast.LENGTH_LONG).show();
        }
    }
}
