package co.edu.udea.compumovil.gr06.shareit.UI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.edu.udea.compumovil.gr06.shareit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class prueba extends Fragment {


    public prueba() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prueba, container, false);
    }

}
