package co.edu.udea.compumovil.gr06.shareit.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import co.edu.udea.compumovil.gr06.shareit.R;
import co.edu.udea.compumovil.gr06.shareit.UI.Utilities.Utilidades;
import co.edu.udea.compumovil.gr06.shareit.UI.daos.CommentUserDAO;
import co.edu.udea.compumovil.gr06.shareit.UI.model.CommentUser;

/**
 * Created by jaime on 14/11/2016.
 */

public class DialogAgregarComentario extends DialogFragment {


    public DialogAgregarComentario() {
        super();
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        return crearDialogAgregarComentario();
    }

    public android.app.Dialog crearDialogAgregarComentario() {
        final AlertDialog.Builder dialogo = new AlertDialog.Builder(getActivity());
        LayoutInflater inflate = getActivity().getLayoutInflater();
        final View vista = inflate.inflate(R.layout.dialog_agregar_comentario, null);
        dialogo.setView(vista);
        Button aceptar = (Button) vista.findViewById(R.id.aceptar_comentario);
        Button cancelar = (Button) vista.findViewById(R.id.cancelar_comentario);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText comentario = (EditText) vista.findViewById(R.id.comentario_del_usuario);
                RatingBar score = (RatingBar) vista.findViewById(R.id.score_comentario);
                if (!comentario.getText().toString().isEmpty()) {
                    CommentUser nuevo = new CommentUser();
                    nuevo.setFecha(Utilidades.obtenerFechaActual(getContext()));
                    nuevo.setComentario(comentario.getText().toString());
                    nuevo.setScore(score.getRating());
                    FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
                    if (usuario != null) {
                        nuevo.setAutor(usuario.getDisplayName());
                    }
                    CommentUserDAO add = new CommentUserDAO();
                    add.addComment(nuevo);
                    dismiss();
                } else {
                    comentario.setError(getString(R.string.dia_message_campo_requerido));
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
