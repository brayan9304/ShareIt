package co.edu.udea.compumovil.gr06.shareit.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import co.edu.udea.compumovil.gr06.shareit.R;
import co.edu.udea.compumovil.gr06.shareit.UI.Utilities.Utilidades;

public class NewAccounts extends AppCompatActivity {

    private static final String TAG = "NewAccounts";
    private static final int ACTION_CAMERA = 0;
    private static final int ACTION_GALLERY = 1;
    private static final String CARPETA_IMAGENES_USUARIO = "Imagenes de usuarios";
    private String path;
    private ByteArrayInputStream flujo;

    //FIREBASE
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference cubeta, carpeta;
    private View container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_accounts);

        path = "";
        flujo = null;
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        cubeta = storage.getReferenceFromUrl("gs://share-it-40aed.appspot.com");
        carpeta = cubeta.child(CARPETA_IMAGENES_USUARIO);
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
                                            final FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
                                            UserProfileChangeRequest cambio = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(diplayName)
                                                    .build();
                                            usuario.updateProfile(cambio);
                                            if (flujo != null) {
                                                cargarImagenUsuario(ACTION_CAMERA, usuario);
                                            } else if (!path.isEmpty()) {
                                                cargarImagenUsuario(ACTION_GALLERY, usuario);
                                            }

                                            Toast.makeText(getApplicationContext(), R.string.SuccessCreation, Toast.LENGTH_SHORT).show();


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


    public void buscarImagen(View vista) {
        mostrarDialogRecursos().show();
    }

    public AlertDialog mostrarDialogRecursos() {
        AlertDialog.Builder mensaje = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.dialog));
        final String[] recursos = getResources().getStringArray(R.array.recursosImage);
        mensaje.setTitle(R.string.tituloRecursosImagen)
                .setItems(recursos, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            startActivityForResult(intent, ACTION_CAMERA);
                        } else {
                            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            galleryIntent.setType("image/*");
                            startActivityForResult(galleryIntent, ACTION_GALLERY);
                        }
                    }
                });
        return mensaje.create();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ACTION_GALLERY) {
            if (data != null) {
                Uri extras = data.getData();
                Bitmap imageBitmap = null;
                path = Utilidades.getPath(this, extras);

                Log.e(TAG, "onActivityResult: " + path);
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), extras);
                } catch (IOException e) {

                }
                ImageView picture = (ImageView) findViewById(R.id.ImagenIntent);
                picture.setImageBitmap(imageBitmap);
            }
        }

        if (resultCode == RESULT_OK && requestCode == ACTION_CAMERA) {
            if (data != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                ImageView picture = (ImageView) findViewById(R.id.ImagenIntent);
                picture.setImageBitmap(imageBitmap);
                ByteArrayOutputStream salida = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, salida);
                byte[] datos = salida.toByteArray();
                flujo = new ByteArrayInputStream(datos);
            }
        }
    }

    public void cargarImagenUsuario(int modo, final FirebaseUser usuario) {
        if (modo == ACTION_CAMERA) {
            StorageReference archivo = carpeta.child(usuario.getUid() + ".png");
            UploadTask uploadTask = archivo.putStream(flujo);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.e(TAG, "onSuccess: " + "no se pudo subir");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Log.e(TAG, "onSuccess: " + downloadUrl.toString());
                    UserProfileChangeRequest cambio = new UserProfileChangeRequest.Builder().setPhotoUri(downloadUrl).build();
                    usuario.updateProfile(cambio);
                    mAuth.signOut();
                    finish();
                }
            });
        }
        if (modo == ACTION_GALLERY) {
            StorageReference archivo = carpeta.child(usuario.getUid() + ".png");
            InputStream stream = null;
            try {
                stream = new FileInputStream(new File(path));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            UploadTask uploadTask = archivo.putStream(stream);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.e(TAG, "onSuccess: " + "no se pudo subir");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Log.e(TAG, "onSuccess: " + downloadUrl.toString());
                    UserProfileChangeRequest cambio = new UserProfileChangeRequest.Builder().setPhotoUri(downloadUrl).build();
                    usuario.updateProfile(cambio);
                    mAuth.signOut();
                    finish();
                }
            });
        }
    }

}


