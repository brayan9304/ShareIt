package co.edu.udea.compumovil.gr06.shareit.UI;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
    private static final int MY_EXTERNAL_WRITE_READ = 1;
    private String path;
    private byte[] datos;
    private ByteArrayInputStream flujo;

    //FIREBASE
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference cubeta, carpeta;
    private View container;
    private Bitmap imageBitmap;
    private static final String STATE_PHOTO = "photo";
    private static final String STATE_PATH = "path";
    private static final String STATE_FLUJO = "flujo";

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

        if (savedInstanceState != null) {
            imageBitmap = savedInstanceState.getParcelable(STATE_PHOTO);
            ImageView picture = (ImageView) findViewById(R.id.ImagenIntent);
            if (imageBitmap == null) {
                picture.setImageResource(R.drawable.ic_insert_photo_black_48dp);
            } else {
                picture.setImageBitmap(imageBitmap);
            }
            path = savedInstanceState.getString(STATE_PATH);
            String tempo = savedInstanceState.getString(STATE_FLUJO);
            byte[] bytes = savedInstanceState.getByteArray(STATE_FLUJO);
            if (bytes != null) {
                flujo = new ByteArrayInputStream(bytes);
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_PHOTO, imageBitmap);
        outState.putString(STATE_PATH, path);
        outState.putByteArray(STATE_FLUJO, datos);
        super.onSaveInstanceState(outState);
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

    private void verificarPermisos() {

        int writePermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            writePermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            solicitarPermiso();
        } else {
            mostrarDialogRecursos().show();
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
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_EXTERNAL_WRITE_READ);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Si el requestCode corresponde al que usamos para solicitar el permiso y
        //la respuesta del usuario fue positiva
        if (requestCode == MY_EXTERNAL_WRITE_READ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mostrarDialogRecursos().show();
            } else {
                mostrarSnackBar();
            }
        }
    }

    private void mostrarSnackBar() {
        Snackbar.make(container, R.string.permisssion_external_storage,
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
                claveView.setError(getString(R.string.fault_login));
                claveView.requestFocus();
            }
            if (e.getErrorCode().equals(LoginShareIt.INVALID_EMAIL_FORMAT)) {
                correoView.setError(getString(R.string.fault_login_invalid_email_format));
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
        verificarPermisos();
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
                imageBitmap = (Bitmap) data.getExtras().get("data");
                ImageView picture = (ImageView) findViewById(R.id.ImagenIntent);
                picture.setImageBitmap(imageBitmap);
                ByteArrayOutputStream salida = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, salida);
                datos = salida.toByteArray();
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


