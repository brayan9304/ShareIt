package co.edu.udea.compumovil.gr06.shareit.UI;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr06.shareit.R;
import co.edu.udea.compumovil.gr06.shareit.UI.Utilities.operacionCalPromedio;
import co.edu.udea.compumovil.gr06.shareit.UI.adapter.ComentarioAdapter;
import co.edu.udea.compumovil.gr06.shareit.UI.daos.ProductDAO;
import co.edu.udea.compumovil.gr06.shareit.UI.model.CommentUser;
import co.edu.udea.compumovil.gr06.shareit.UI.model.Product;
import co.edu.udea.compumovil.gr06.shareit.UI.model.Promedio;

public class Comentarios extends AppCompatActivity implements operacionCalPromedio {

    private static final String TAG = "COMENTARIOS";
    private RecyclerView listComentarios;
    private List<CommentUser> comentarios;
    private Promedio comentPromedio;
    private ProgressDialog progressDialog;
    private ComentarioAdapter comentariosAdapter;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        progressDialog = ProgressDialog.show(this, getString(R.string.messege_wait),
                getString(R.string.message_cargando), true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        comentPromedio = new Promedio();
        listComentarios = (RecyclerView) findViewById(R.id.reciclerComentarios);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listComentarios.setLayoutManager(linearLayoutManager);
        comentarios = new ArrayList<>();
        comentariosAdapter = new ComentarioAdapter(comentarios);
        listComentarios.setAdapter(comentariosAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogAgregarComentario().show(getSupportFragmentManager(), "Comentario");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        myRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference comentariosPorProducto = myRef.child(CommentUser.CHILD);
        comentariosPorProducto.keepSynced(true);
        DatabaseReference comentariosDeUnProducto = comentariosPorProducto.child(Product.getProduct().getKey());
        comentariosDeUnProducto.keepSynced(true);
        comentariosDeUnProducto.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    CommentUser cargado = dataSnapshot.getValue(CommentUser.class);
                    sumarYAumentar(cargado.getScore());
                    comentariosAdapter.rellenarAdapter(cargado);
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
        progressDialog.dismiss();
    }

    @Override
    protected void onPause() {
        comentarios = new ArrayList<>();
        comentariosAdapter = new ComentarioAdapter(comentarios);
        listComentarios.setAdapter(comentariosAdapter);
        super.onPause();
    }

    @Override
    protected void onRestart() {
        comentarios = new ArrayList<>();
        comentariosAdapter = new ComentarioAdapter(comentarios);
        listComentarios.setAdapter(comentariosAdapter);
        super.onRestart();
    }

    @Override
    public void sumarYAumentar(double suma) {
        comentPromedio.aumentarCantidad();
        comentPromedio.sumarMas(suma);
        comentPromedio.calPromedio();
        Log.e(TAG, "sumarYAumentar: " + comentPromedio.getPromedio());
        Product modificado = Product.getProduct();
        modificado.setCalification((float) comentPromedio.getPromedio());
        ProductDAO.updateProduct(modificado);
    }
}
