package co.edu.udea.compumovil.gr06.shareit.UI;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
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
import co.edu.udea.compumovil.gr06.shareit.UI.Localizacion.Location;
import co.edu.udea.compumovil.gr06.shareit.UI.daos.ProductDAO;
import co.edu.udea.compumovil.gr06.shareit.UI.model.Product;

import static android.app.Activity.RESULT_OK;


public class FragmentCompartir extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    static final String STATE_PHOTO = "imageBitmap";
    static final String STATE_NAME = "product_name";
    static final String STATE_TYPE = "productType";
    static final String STATE_PRICE = "price";
    static final String STATE_DESCRIPTION = "description";
    static final String STATE_RATING = "ratingBar";
    static final String STATE_PATH = "path";


    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int ACTION_CAMERA = 0;
    private static final int MY_LOCATION = 1;
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
    static Location origin;
    private static View mLinearStatic;
    private LocationManager mangLocation;
    private LocationListener listLocation;
    private View mLinear;


    private FirebaseStorage storage;
    private StorageReference cubeta, carpeta;
    private static String path;
    private GoogleApiClient mGoogleApiClient;
    private android.location.Location mLastLocation;
    private View fragmento;


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
        savedInstanceState.putString(STATE_PATH, path);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmento = inflater.inflate(R.layout.fragment_compartir, container, false);
        productType = (Spinner) fragmento.findViewById(R.id.eTProductType);
        price = (EditText) fragmento.findViewById(R.id.precio);
        description = (EditText) fragmento.findViewById(R.id.description);
        product_name = (EditText) fragmento.findViewById(R.id.nameProduct);
        productPicture = (ImageView) fragmento.findViewById(R.id.productPicture);
        ratingBar = (RatingBar) fragmento.findViewById(R.id.rating);
        cargarFoto = (FloatingActionButton) fragmento.findViewById(R.id.boton_foto_productos);
        cargarFoto.setOnClickListener(this);
        path = "";
        storage = FirebaseStorage.getInstance();
        cubeta = storage.getReferenceFromUrl("gs://share-it-40aed.appspot.com");
        carpeta = cubeta.child("Imagenes Producto");
        mLinearStatic = fragmento.findViewById(R.id.layput_scroll_compartir);
        mLinear = fragmento.findViewById(R.id.layput_scroll_compartir);

        if (savedInstanceState != null) {
            ratingBar.setRating(savedInstanceState.getFloat(STATE_RATING));
            price.setText(savedInstanceState.getString(STATE_PRICE));
            description.setText(savedInstanceState.getString(STATE_DESCRIPTION));
            product_name.setText(savedInstanceState.getString(STATE_NAME));
            imageBitmap = savedInstanceState.getParcelable(STATE_PHOTO);
            productType.setSelection(savedInstanceState.getInt(STATE_TYPE));
            path = savedInstanceState.getString(STATE_PATH);
            if (imageBitmap == null) {
                productPicture.setImageResource(R.drawable.ic_action_add_photo);
            } else {
                productPicture.setImageBitmap(imageBitmap);
            }


        }


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(fragmento.getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        return fragmento;
    }

    @Override
    public void onStart() {
        verificarPermisos();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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

    public static void save(final Context context) {
        byte[] uPicture;
        ByteArrayOutputStream bitesOut = new ByteArrayOutputStream();

        if (imageBitmap == null) {
            uPicture = null;
        } else {
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bitesOut);
            uPicture = bitesOut.toByteArray();
        }

        if (path.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.message_photo_required), Toast.LENGTH_LONG).show();
        } else if (product_name.getText().toString().isEmpty()) {
            Toast.makeText(context, context.getString(R.string.messege_product_name_required), Toast.LENGTH_LONG).show();
        } else if (ratingBar.getRating() == 0) {
            Toast.makeText(context, context.getString(R.string.message_score_required), Toast.LENGTH_LONG).show();
        } else if (price.getText().toString().isEmpty()) {
            Toast.makeText(context, context.getString(R.string.message_price_required), Toast.LENGTH_LONG).show();
        } else if (description.getText().toString().isEmpty()) {
            Toast.makeText(context, context.getString(R.string.message_description_required), Toast.LENGTH_LONG).show();
        } else {
            if (productType.getSelectedItem().toString().equals("Selecciona Uno")) {
                Toast.makeText(context, context.getResources().getString(R.string.message_product_type), Toast.LENGTH_LONG).show();
            } else {
                if (origin != null) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    productDAO = new ProductDAO();
                    product = new Product();
                    product.setNameUser(currentUser.getDisplayName());
                    product.setEmail(currentUser.getEmail());
                    product.setProduct_type(productType.getSelectedItem().toString());
                    product.setPrice(Integer.parseInt(price.getText().toString()));
                    product.setDescription(description.getText().toString());
                    product.setProductName(product_name.getText().toString());
                    product.setPathPoto(path);
                    product.setCalification(ratingBar.getRating());
                    product.setLatitudPosicion(origin.getLatitud());
                    product.setLongitudPosicion(origin.getLongitud());
                    productDAO.addProduct(product);
                    Toast.makeText(context, context.getString(R.string.messege_new_product), Toast.LENGTH_LONG).show();
                    price.setText("");
                    description.setText("");
                    product_name.setText("");
                    ratingBar.setRating(0);
                    productType.setSelection(0);
                    productPicture.setImageResource(R.drawable.ic_action_add_photo);
                } else {
                    Snackbar.make(mLinearStatic, R.string.permission_location,
                            Snackbar.LENGTH_LONG)
                            .setAction(R.string.settings, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                                    context.startActivity(intent);
                                }
                            })
                            .show();
                }
            }
        }
    }

    private void verificarPermisos() {

        int writePermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            writePermission = getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            solicitarPermiso();
        } else {
            mGoogleApiClient.connect();
        }
    }

    private void solicitarPermiso() {
        //shouldShowRequestPermissionRationale es verdadero solamente si ya se había mostrado
        //anteriormente el dialogo de permisos y el usuario lo negó
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            mostrarSnackBar();
        } else {
            //si es la primera vez se solicita el permiso directamente
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Si el requestCode corresponde al que usamos para solicitar el permiso y
        //la respuesta del usuario fue positiva
        if (requestCode == MY_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mGoogleApiClient.connect();
            } else {
                mostrarSnackBar();
            }
        }
    }

    private void mostrarSnackBar() {
        Snackbar.make(mLinear, R.string.permission_location,
                Snackbar.LENGTH_LONG)
                .setAction(R.string.settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        abrirConfiguracion();
                    }
                })
                .show();
    }

    public void abrirConfiguracion() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
        startActivity(intent);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                origin = new Location(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                Toast.makeText(getContext(), getResources().getString(R.string.message_localizacion_add), Toast.LENGTH_SHORT).show();
            }
        }catch (SecurityException e){
            Log.d("SecurityException", "fragmentCompartir");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), getResources().getString(R.string.message_no_location), Toast.LENGTH_LONG).show();
    }
}
