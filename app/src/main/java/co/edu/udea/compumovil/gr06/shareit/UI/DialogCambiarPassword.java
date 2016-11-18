package co.edu.udea.compumovil.gr06.shareit.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import co.edu.udea.compumovil.gr06.shareit.R;

/**
 * Created by jaime on 14/11/2016.
 */

public class DialogCambiarPassword extends DialogFragment {


    public DialogCambiarPassword() {
        super();
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        return crearDialogCambiarPass();
    }

    public android.app.Dialog crearDialogCambiarPass() {
        final AlertDialog.Builder dialogo = new AlertDialog.Builder(getActivity());
        LayoutInflater inflate = getActivity().getLayoutInflater();
        final View vista = inflate.inflate(R.layout.dialog_cambiar_contrasena, null);
        dialogo.setView(vista);
        Button aceptar = (Button) vista.findViewById(R.id.dia_btn_aceptar);
        Button cancelar = (Button) vista.findViewById(R.id.dia_btn_cancelar);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseAuth mAuto = FirebaseAuth.getInstance();
                final FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    }
                };
                mAuto.addAuthStateListener(mAuthListener);
                final FirebaseUser userActual = FirebaseAuth.getInstance().getCurrentUser();
                if (userActual != null) {
                    TextView actualPass = (TextView) vista.findViewById(R.id.dia_actual_contraseña);
                    String contraActual = actualPass.getText().toString();
                    final TextView nuevaPass = (TextView) vista.findViewById(R.id.dia_nueva_contraseña);
                    final TextView confirmPass = (TextView) vista.findViewById(R.id.dia_conf_contraseña);
                    final String nueva = nuevaPass.getText().toString();
                    final String confirmar = confirmPass.getText().toString();
                    String correo = userActual.getEmail();
                    if (nueva.equals("") || confirmar.equals("") || contraActual.equals("")) {
                        Snackbar.make(vista, getString(R.string.dia_message_campos_requ), Snackbar.LENGTH_SHORT).show();
                        if (nueva.equals("")) {
                            nuevaPass.setError(getString(R.string.dia_message_campo_requerido));
                        }
                        if (confirmar.equals("")) {
                            confirmPass.setError(getString(R.string.dia_message_campo_requerido));
                        }
                        if (contraActual.equals("")) {
                            actualPass.setError(getString(R.string.dia_message_campo_requerido));
                            actualPass.requestFocus();
                        }
                    } else {
                        mAuto.signInWithEmailAndPassword(correo, contraActual).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (nueva.length() > 8) {
                                        if (nueva.equals(confirmar)) {
                                            userActual.updatePassword(nueva);
                                            mAuto.removeAuthStateListener(mAuthListener);
                                            Toast.makeText(getContext(), getString(R.string.dia_message_success), Toast.LENGTH_SHORT).show();
                                            dismiss();
                                        } else {
                                            Snackbar.make(vista, getString(R.string.dig_message_nocoinciden), Snackbar.LENGTH_SHORT).show();
                                            confirmPass.requestFocus();
                                            confirmPass.setText("");
                                        }
                                    } else {
                                        nuevaPass.setError(getString(R.string.dia_message_shortLength));
                                        Snackbar.make(vista, getString(R.string.dig_message_shortPass), Snackbar.LENGTH_SHORT).show();

                                    }
                                } else {
                                    Snackbar.make(vista, getString(R.string.dia_message_incorrectaActual), Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return dialogo.create();
    }

}
