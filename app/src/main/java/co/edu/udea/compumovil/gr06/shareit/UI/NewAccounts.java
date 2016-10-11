package co.edu.udea.compumovil.gr06.shareit.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import co.edu.udea.compumovil.gr06.shareit.R;

public class NewAccounts extends AppCompatActivity {

    private static final String TAG = "NewAccounts";

    //FIREBASE
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_accounts);

        mAuth = FirebaseAuth.getInstance();

    }

    public void crearCuenta(View vista) {

    }

    public boolean validarCampo(EditText campo) {
        campo.setError(null);
        String valor = campo.getText().toString();
        if (valor.isEmpty()) {
            campo.setError(getString(R.string.requerid_camp));
            campo.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public boolean validarContraseñas(String pass, String repPass) {
        return pass == repPass;
    }

}
