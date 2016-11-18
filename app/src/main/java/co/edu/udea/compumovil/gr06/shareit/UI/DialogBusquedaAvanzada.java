package co.edu.udea.compumovil.gr06.shareit.UI;

import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import co.edu.udea.compumovil.gr06.shareit.R;

/**
 * Created by brayan on 16/11/16.
 */

public class DialogBusquedaAvanzada extends DialogFragment {
    LayoutInflater inflater;
    View view;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_busqueda_avanzada, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view).setPositiveButton("BUSCAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Spinner type = (Spinner) view.findViewById(R.id.searchType);
                RatingBar searchRAting = (RatingBar) view.findViewById(R.id.searchRating);
                EditText eTMin = (EditText) view.findViewById(R.id.min);
                EditText eTMax = (EditText) view.findViewById(R.id.max);
                String sMin = eTMin.getText().toString();
                String sMax = eTMax.getText().toString();
                int min;
                int max;
                String sType = type.getSelectedItem().toString();
                float rating = searchRAting.getRating();

                if (!sMin.isEmpty() && !sMax.isEmpty()) {
                    min = Integer.parseInt(sMin);
                    max = Integer.parseInt(sMax);
                    if (min < max) {

                        if (!sType.equals("Ninguno") && rating != 0.0) {
                            SearchFragment.search(min, max, sType, rating);
                        } else if (!sType.equals("Ninguno")) {
                            SearchFragment.search(min, max, sType);
                        } else if (rating != 0.0) {
                            SearchFragment.search(min, max, rating);
                        } else {
                            SearchFragment.search(min, max);
                        }
                    }else{
                        Toast.makeText(getContext(),"Min debe ser menor que max", Toast.LENGTH_LONG).show();
                    }
                } else{
                    if (!sType.equals("Ninguno") && rating != 0.0) {
                        SearchFragment.search(sType, rating);
                    } else if (!sType.equals("Ninguno")) {
                        SearchFragment.searchType(sType);
                    }else if (rating != 0.0){
                        SearchFragment.search(rating);
                    }
                }

            }
        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}
