package co.edu.udea.compumovil.gr06.shareit.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import co.edu.udea.compumovil.gr06.shareit.R;
import co.edu.udea.compumovil.gr06.shareit.UI.model.Product;

public class ViewProductCard extends AppCompatActivity {
    private Button verComentarios;
    private Button verLocalizacion;
    private ImageView imageView;
    private RatingBar rating;
    private TextView productName, productType, description, price, userName;
    private Product product;

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

        loadView();

        // OnCLick Listener
        verComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Crear intent
            }
        });
        verLocalizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Crear intent
            }
        });

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
