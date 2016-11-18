package co.edu.udea.compumovil.gr06.shareit.UI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import co.edu.udea.compumovil.gr06.shareit.R;
import co.edu.udea.compumovil.gr06.shareit.UI.Utilities.operacionCalPromedio;
import co.edu.udea.compumovil.gr06.shareit.UI.model.Product;
import co.edu.udea.compumovil.gr06.shareit.UI.model.Promedio;

/**
 * A simple {@link Fragment} subclass.
 */
public class Perfil extends Fragment implements operacionCalPromedio {

    private Promedio miPromedio;
    private RatingBar reputacion;


    public Perfil() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_perfil, container, false);
        ImageView imgPerfil = (ImageView) fragmentView.findViewById(R.id.img_perfil);
        TextView nombrePerfil = (TextView) fragmentView.findViewById(R.id.txt_nombre_perfil);
        TextView correoPerfil = (TextView) fragmentView.findViewById(R.id.txt_correo_perfil);
        reputacion = (RatingBar) fragmentView.findViewById(R.id.rating_reputacion);
        FirebaseUser usuarioActivo = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioActivo != null) {
            Picasso.with(fragmentView.getContext()).load(usuarioActivo.getPhotoUrl()).resize(200, 200).into(imgPerfil);
            nombrePerfil.setText(usuarioActivo.getDisplayName());
            correoPerfil.setText(usuarioActivo.getEmail());
        }

        Button cambiar = (Button) fragmentView.findViewById(R.id.btn_cambiar_contraseña);
        cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogCambiarPassword().show(getFragmentManager(), "cambiar");
            }
        });
        miPromedio = new Promedio();
        calcularReputacion();
        int s = 0;
        return fragmentView;
    }

    public void calcularReputacion() {
        DatabaseReference myRef;
        myRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mensajeRef = myRef.child(Product.CHILD);
        FirebaseUser userActivo = FirebaseAuth.getInstance().getCurrentUser();
        final String correoUser = userActivo.getEmail();
        mensajeRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product temp = dataSnapshot.getValue(Product.class);
                String usuario = temp.getEmail();
                if (usuario.equalsIgnoreCase(correoUser)) {
                    sumarYAumentar(temp.getCalification());
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void sumarYAumentar(double suma) {
        miPromedio.aumentarCantidad();
        miPromedio.sumarMas(suma);
        miPromedio.calPromedio();
        reputacion.setRating((float) miPromedio.getPromedio());

    }
}
