package co.edu.udea.compumovil.gr06.shareit.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;

import co.edu.udea.compumovil.gr06.shareit.R;


interface actions {
    void enRespuetaPositiva();

    void enRespuestaNegativa();
}

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, actions {

    public static String STATE_VISIBILITY;

    private Fragment compartir, buscar, acercaDe;
    private static final String TAG = "MainActivity";
    private FirebaseUser usuarioActivo;
    private FirebaseAuth mAuth;
    private LinearLayout headSearch;
    private LinearLayout headShare;
    private ImageButton done;
    private EditText name;
    private ImageButton iBSearch;
    private ImageButton iBconfig;
    private EditText eTSearch;
    int id;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt(STATE_VISIBILITY,id);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        headSearch = (LinearLayout)findViewById(R.id.headSearch);
        headShare = (LinearLayout)findViewById(R.id.headShare);
        done = (ImageButton)findViewById(R.id.done);
        name = (EditText)findViewById(R.id.nameProduct);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentCompartir.save(getApplicationContext());
            }
        });
        iBconfig = (ImageButton) findViewById(R.id.config);
        iBconfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBusquedaAvanzada busquedaAvanzada = new DialogBusquedaAvanzada();
                busquedaAvanzada.show(getSupportFragmentManager(),"DialogBusquedaAvanzada");
            }
        });
        eTSearch = (EditText) findViewById(R.id.ETSearch);
        iBSearch = (ImageButton)findViewById(R.id.IBSearch);
        iBSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment.search(eTSearch.getText().toString());
            }
        });
        if(savedInstanceState != null){
            id = savedInstanceState.getInt(STATE_VISIBILITY);
            if (id == R.id.nav_buscar) {
                headShare.setVisibility(View.INVISIBLE);
                headSearch.setVisibility(View.VISIBLE);

                // Handle the camera action
            } else if (id == R.id.nav_compartir) {
                headSearch.setVisibility(View.INVISIBLE);
                headShare.setVisibility(View.VISIBLE);


            } else if (id == R.id.nav_acercade) {
                headSearch.setVisibility(View.INVISIBLE);
                headShare.setVisibility(View.INVISIBLE);

            }
        }
        //setSupportActionBar(toolbar);



    /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hNave = navigationView.getHeaderView(0);
        RoundedImageView imagenUsuario = (RoundedImageView) hNave.findViewById(R.id.Imagen_usuario);
        try {
            InputStream temp = getAssets().open("Images/forum-user.png");
            Bitmap imageTemp = BitmapFactory.decodeStream(temp);
            imagenUsuario.setImageBitmap(imageTemp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Picasso.with(getApplicationContext()).load("Images/forum-user.png").into(imagenUsuario);

        mAuth = FirebaseAuth.getInstance();
        usuarioActivo = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioActivo != null) {
            Log.d(TAG, "onAuthStateChanged:signed_in:" + usuarioActivo.getDisplayName());
            Uri e = null;
            for (UserInfo usuario : usuarioActivo.getProviderData()) {
                e = usuario.getPhotoUrl();
            }
            TextView nombreUsuarios = (TextView) hNave.findViewById(R.id.ShareIt_nav);
            nombreUsuarios.setText(usuarioActivo.getDisplayName());
            if (e != null) {
                Picasso.with(getApplicationContext()).load(e.toString()).into(imagenUsuario);
            } else {
                e = usuarioActivo.getPhotoUrl();
                    if(e!=null) {
                        Picasso.with(getApplicationContext()).load(e.toString()).into(imagenUsuario);
                    }
            }
        } else {
            Log.d(TAG, "onAuthStateChanged:signed_in: null");
        }

        buscar = new SearchFragment();
        compartir = new FragmentCompartir();
        acercaDe = new AcercaDe();

        if (savedInstanceState == null) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.container, compartir, "compartir");
            transaction.commit();

        }

    }

    @Override
    public void enRespuetaPositiva() {
        mAuth.signOut();
        Intent cerrar = new Intent(getApplicationContext(), LoginShareIt.class);
        startActivity(cerrar);
        this.finish();
    }

    @Override
    public void enRespuestaNegativa() {

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            mostrarDialog().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        id = item.getItemId();

        if (id == R.id.nav_buscar) {
            Log.e("rer", "onNavigationItemSelected: buscar");
            headShare.setVisibility(View.INVISIBLE);
            headSearch.setVisibility(View.VISIBLE);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, buscar);
            transaction.commit();

            // Handle the camera action
        } else if (id == R.id.nav_compartir) {
            Log.e("rer", "onNavigationItemSelected: compartir");
            headSearch.setVisibility(View.INVISIBLE);
            headShare.setVisibility(View.VISIBLE);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, compartir);
            transaction.commit();

        } else if (id == R.id.nav_acercade) {
            headSearch.setVisibility(View.INVISIBLE);
            headShare.setVisibility(View.INVISIBLE);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, acercaDe);
            transaction.commit();

        }
        if (id == R.id.nav_cerrar_sesion) {
            mostrarDialog().show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public AlertDialog mostrarDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.dialog));

        dialog.setTitle(R.string.cerrar)
                .setMessage(R.string.mensaje_cerrar_sesion)
                .setPositiveButton(R.string.respuestaPositiva, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enRespuetaPositiva();
                    }
                })
                .setNegativeButton(R.string.respuestaNegativa, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return dialog.create();
    }
}
