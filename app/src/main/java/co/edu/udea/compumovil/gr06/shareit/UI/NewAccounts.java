package co.edu.udea.compumovil.gr06.shareit.UI;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import co.edu.udea.compumovil.gr06.shareit.R;

public class NewAccounts extends AppCompatActivity {

    private static final String TAG = "NewAccounts";

    //FIREBASE
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private View container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_accounts);

        mAuth = FirebaseAuth.getInstance();
        container = findViewById(R.id.LinearAccount);

    }

    public void crearCuenta(View vista) {
        final EditText correo = (EditText) findViewById(R.id.Correo);
        EditText nombre = (EditText) findViewById(R.id.nombreUsuario);
        EditText apellido = (EditText) findViewById(R.id.ApellidosUsuario);
        final EditText contraseña = (EditText) findViewById(R.id.Contraseña);
        EditText repContraseña = (EditText) findViewById(R.id.ContraseñaRepetida);

        if (validarCampo(correo) & validarCampo(nombre) & validarCampo(apellido) & validarCampo(contraseña)
                & validarCampo(repContraseña)) {

            if (validarContraseñas(contraseña) & validarContraseñas(repContraseña)) {
                final String contra = contraseña.getText().toString();
                String repContra = repContraseña.getText().toString();
                if (validarContraseñasCoinciden(contra, repContra)) {
                    final String correoS = correo.getText().toString();
                    final String diplayName = nombre.getText().toString() + " " + apellido.getText().toString();
                    mAuth.createUserWithEmailAndPassword(correoS, contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                catchFirBaseExceptions(task, correo, contraseña);
                            } else {
                                mAuth.signInWithEmailAndPassword(correoS, contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
                                            UserProfileChangeRequest cambio = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(diplayName)
                                                    .build();
                                            usuario.updateProfile(cambio);
                                            Toast.makeText(getApplicationContext(), R.string.SuccessCreation, Toast.LENGTH_SHORT).show();
                                            mAuth.signOut();
                                            finish();
                                        }
                                    }
                                });
                            }
                        }
                    });


                } else {
                    Snackbar.make(container, R.string.noCoinciden, Snackbar.LENGTH_SHORT).show();
                    repContraseña.setError(getString(R.string.repNoCoincide));
                    repContraseña.requestFocus();
                }
            }


        } else {
            Snackbar.make(container, R.string.required_camps, Snackbar.LENGTH_SHORT).show();
        }
    }

    public void catchFirBaseExceptions(@NonNull Task<AuthResult> task, EditText correoView, EditText claveView) {
        correoView.setError(null);
        claveView.setError(null);
        try {
            throw task.getException();
        } catch (FirebaseNetworkException e) {
            Snackbar.make(container, R.string.without_conection, Snackbar.LENGTH_LONG).show();
        } catch (FirebaseAuthUserCollisionException e) {
            Log.d(TAG, "onComplete: " + e.getErrorCode());
            Snackbar.make(container, R.string.AlreadyRegister, Snackbar.LENGTH_LONG).show();
        } catch (FirebaseAuthInvalidCredentialsException e) {
            if (e.getErrorCode().equals(LoginShareIt.PASSWORD_FIREBASE_ERROR)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    claveView.setError(getString(R.string.fault_login), getDrawable(R.drawable.ic_error_outline_24dp));
                } else {
                    claveView.setError(getString(R.string.fault_login));
                }
                claveView.requestFocus();
            }
            if (e.getErrorCode().equals(LoginShareIt.INVALID_EMAIL_FORMAT)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    correoView.setError(getString(R.string.fault_login_invalid_email_format), getDrawable(R.drawable.ic_error_outline_24dp));
                } else {
                    correoView.setError(getString(R.string.fault_login_invalid_email_format));
                }
                correoView.requestFocus();
            }

        } catch (FirebaseAuthInvalidUserException e) {
            Snackbar.make(container, R.string.fault_login_invalid_User, Snackbar.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
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

    public boolean validarContraseñasCoinciden(String pass, String repPass) {
        return pass.equals(repPass);
    }

    public boolean validarContraseñas(EditText campo) {
        int longitud = campo.getText().toString().length();
        if (longitud < 8) {
            campo.setError(getString(R.string.longitudMinima));
            campo.requestFocus();
            return false;
        }
        return true;
    }

}
