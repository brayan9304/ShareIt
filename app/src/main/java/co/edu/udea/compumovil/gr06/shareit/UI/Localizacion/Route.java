package co.edu.udea.compumovil.gr06.shareit.UI.Localizacion;


import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by jaime on 17/11/2016.
 */
public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
