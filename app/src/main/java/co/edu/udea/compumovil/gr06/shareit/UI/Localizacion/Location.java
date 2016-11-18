package co.edu.udea.compumovil.gr06.shareit.UI.Localizacion;

/**
 * Created by jaime on 17/11/2016.
 */

public class Location {

    public Location(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    private double latitud;
    private double longitud;
}
