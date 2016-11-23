package co.edu.udea.compumovil.gr06.shareit.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import co.edu.udea.compumovil.gr06.shareit.R;

public class LoginShareIt extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, actions {

    private static final String TAG = "LoginShareIt";
    public static final String PASSWORD_FIREBASE_ERROR = "ERROR_WRONG_PASSWORD";
    public static final String INVALID_EMAIL_FORMAT = "ERROR_INVALID_EMAIL";
    private static final int RC_SIGN_IN = 9001;
    private View layoutPrincipal;

    //firebase
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //google sing-in
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = ProgressDialog.show(this, getString(R.string.messege_wait),
                getString(R.string.message_cargando), true);
        setContentView(R.layout.activity_login_share_it);
        EditText correo = (EditText) findViewById(R.id.correo_login);
        EditText clave = (EditText) findViewById(R.id.clave_login);
        layoutPrincipal = findViewById(R.id.activity_login_share_it);
        layoutPrincipal.requestFocus();
        correo.setSingleLine(true);
        clave.setSingleLine(true);
        clave.setTransformationMethod(PasswordTransformationMethod.getInstance());

        //FIREBASE AUTHENTIFICATION

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuario = firebaseAuth.getCurrentUser();
                if (usuario != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + usuario.getUid());
                    enRespuetaPositiva();
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        //Google SIGN-IN

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.OAUTH2_0)).requestEmail().build();
        SignInButton signInButton = (SignInButton) findViewById(R.id.btn_google_login);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                final EditText correoView = (EditText) findViewById(R.id.correo_login);
                final EditText claveView = (EditText) findViewById(R.id.clave_login);
                String correo = correoView.getText().toString();
                String clave = claveView.getText().toString();
                if (validacionCampos(correoView, claveView)) {
                    mAuth.signInWithEmailAndPassword(correo, clave).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                catchFirBaseExceptions(task, correoView, claveView);
                                return;
                            } else {
                                enRespuetaPositiva();
                            }
                        }
                    });
                }
                break;
        }
    }

    public void newAccounts(View vista) {
        Intent nuevaCuenta = new Intent(this, NewAccounts.class);
        Log.e(TAG, "newAccounts: try init");
        startActivity(nuevaCuenta);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            fireBaseWithGoogle(acct);
        } else {
        }
    }

    private void fireBaseWithGoogle(GoogleSignInAccount cuenta) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + cuenta.getId());

        AuthCredential credencial = GoogleAuthProvider.getCredential(cuenta.getIdToken(), null);
        mAuth.signInWithCredential(credencial).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithCredential", task.getException());
                    catchFirBaseExceptionsGoogle(task);
                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                    enRespuetaPositiva();
                }
            }
        });
    }

    public boolean validacionCampos(EditText correoView, EditText claveView) {
        String correo = correoView.getText().toString();
        String clave = claveView.getText().toString();
        correoView.setError(null);
        claveView.setError(null);

        if (!clave.isEmpty() && !correo.isEmpty()) {
            return true;
        } else {
            layoutPrincipal.requestFocus();
            if (correo.isEmpty()) {
                correoView.setError(getString(R.string.requerid_camp));
                if (!clave.isEmpty()) {
                    correoView.requestFocus();
                }
            }
            if (clave.isEmpty()) {
                claveView.setError(getString(R.string.requerid_camp));
                if (!correo.isEmpty()) {
                    claveView.requestFocus();
                }
            }
            Snackbar.make(layoutPrincipal, R.string.required_camps, Snackbar.LENGTH_SHORT).show();
            return false;
        }
    }

    public void catchFirBaseExceptions(@NonNull Task<AuthResult> task, EditText correoView, EditText claveView) {
        correoView.setError(null);
        claveView.setError(null);
        try {
            throw task.getException();
        } catch (FirebaseNetworkException e) {
            Snackbar.make(layoutPrincipal, R.string.without_conection, Snackbar.LENGTH_LONG).show();
        } catch (FirebaseAuthInvalidCredentialsException e) {
            Log.d(TAG, "onComplete: " + e.getErrorCode());
            if (e.getErrorCode().equals(PASSWORD_FIREBASE_ERROR)) {
                claveView.setError(getString(R.string.fault_login));
                claveView.requestFocus();
            }
            if (e.getErrorCode().equals(INVALID_EMAIL_FORMAT)) {
                correoView.setError(getString(R.string.fault_login_invalid_email_format));
                correoView.requestFocus();
            }

        } catch (FirebaseAuthInvalidUserException e) {
            Snackbar.make(layoutPrincipal, R.string.fault_login_invalid_User, Snackbar.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void catchFirBaseExceptionsGoogle(@NonNull Task<AuthResult> task) {
        try {
            throw task.getException();
        } catch (FirebaseNetworkException e) {
            Snackbar.make(layoutPrincipal, R.string.without_conection, Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        progressDialog.dismiss();
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            if (mGoogleApiClient.isConnected()) {
            }
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }

    @Override
    public void enRespuetaPositiva() {
        Toast.makeText(getApplicationContext(), R.string.success_loggin, Toast.LENGTH_SHORT).show();
        Intent iniciarSesion = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(iniciarSesion);
        this.finish();
    }

    @Override
    public void enRespuestaNegativa() {

    }
}
