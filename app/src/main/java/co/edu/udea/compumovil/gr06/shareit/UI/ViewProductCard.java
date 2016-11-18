package co.edu.udea.compumovil.gr06.shareit.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import co.edu.udea.compumovil.gr06.shareit.R;
import co.edu.udea.compumovil.gr06.shareit.UI.model.Product;

public class ViewProductCard extends AppCompatActivity {
    private static final int MY_LOCATION = 1;
    private Button verComentarios;
    private Button verLocalizacion;
    private ImageView imageView;
    private RatingBar rating;
    private TextView productName, productType, description, price, userName;
    private Product product;
    private View mLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        product = Product.getProduct();
        setContentView(R.layout.activity_view_product_card);
        verComentarios = (Button) findViewById(R.id.buttonComentarios);
        verLocalizacion = (Button) findViewById(R.id.buttonLocalization);
        imageView = (ImageView) findViewById(R.id.ViewPhotoProduct);
        rating = (RatingBar) findViewById(R.id.ratingView);
        productName = (TextView) findViewById(R.id.ViewProductName);
        productType = (TextView) findViewById(R.id.ViewProductType);
        description = (TextView) findViewById(R.id.ViewProductDescription);
        price = (TextView) findViewById(R.id.ViewPriceProduct);
        userName = (TextView) findViewById(R.id.ViewUserName);
        mLinear = findViewById(R.id.activity_view_product_card);

        loadView();

        // OnCLick Listener
        verComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comentarios = new Intent(getApplicationContext(), Comentarios.class);
                startActivity(comentarios);
            }
        });
        verLocalizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarPermisos();
            }
        });

    }

    private void verificarPermisos() {

        int writePermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            writePermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            solicitarPermiso();
        } else {
            Intent mapa = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(mapa);
        }
    }

    private void solicitarPermiso() {
        //shouldShowRequestPermissionRationale es verdadero solamente si ya se había mostrado
        //anteriormente el dialogo de permisos y el usuario lo negó
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
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
                Intent mapa = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(mapa);
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
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        loadView();
        super.onResume();
    }

    private void loadView() {
        Picasso.with(getApplicationContext()).load(product.getPathPoto()).resize(450, 350).into(imageView);
        rating.setRating(product.getCalification());
        productName.setText(product.getProductName());
        productType.setText(product.getProduct_type());
        description.setText(product.getDescription());
        price.setText(Integer.toString(product.getPrice()));
        userName.setText(product.getNameUser());
    }
}
