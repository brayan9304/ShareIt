package co.edu.udea.compumovil.gr06.shareit.UI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import co.edu.udea.compumovil.gr06.shareit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SerachFragment extends Fragment implements RangeSeekBar.OnRangeSeekBarChangeListener {

    Spinner valoracion;
    AutoCompleteTextView tipoProducto;
    RangeSeekBar<Integer> precio;
    View fragment;


    public SerachFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragment = inflater.inflate(R.layout.fragment_serach, container, false);

        precio = (RangeSeekBar<Integer>) fragment.findViewById(R.id.seekBar);
        precio.setRangeValues(100000,20000000);

        precio.setOnRangeSeekBarChangeListener(this);
        valoracion = (Spinner) fragment.findViewById(R.id.spinValoración);
        tipoProducto = (AutoCompleteTextView) fragment.findViewById(R.id.AutoProducto);

        ArrayAdapter<CharSequence> puntuacion = ArrayAdapter.createFromResource(fragment.getContext() ,R.array.valoraciones, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> productos = ArrayAdapter.createFromResource(fragment.getContext(),R.array.productos,android.R.layout.simple_dropdown_item_1line);
        puntuacion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        valoracion.setAdapter(puntuacion);
        tipoProducto.setAdapter(productos);

        return fragment;
    }

    @Override
    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
        Toast.makeText(fragment.getContext(),minValue +" - "+ maxValue,Toast.LENGTH_SHORT).show();

    }
}
