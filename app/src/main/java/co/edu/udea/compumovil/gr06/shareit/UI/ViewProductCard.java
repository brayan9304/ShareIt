package co.edu.udea.compumovil.gr06.shareit.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import co.edu.udea.compumovil.gr06.shareit.R;

public class ViewProductCard extends AppCompatActivity {
    private Button verComentarios;
    private Button verLocalizacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_card);
        verComentarios = (Button)findViewById(R.id.buttonComentarios);
        verLocalizacion = (Button)findViewById(R.id.buttonLocalization);

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
}
