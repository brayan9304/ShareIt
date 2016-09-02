package co.edu.udea.compumovil.gr06.shareit.UI;


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import co.edu.udea.compumovil.gr06.shareit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AcercaDe extends Fragment {

    ImageView logo;

    public AcercaDe() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_acerca_de, container, false);

        logo = (ImageView) fragment.findViewById(R.id.imagenLogo);
        AssetManager asset = fragment.getContext().getAssets();
        try {
            InputStream is = asset.open("Images/logo-udea.png");
            Bitmap imagen = BitmapFactory.decodeStream(is);
            logo.setImageBitmap(imagen);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fragment;
    }

}
