package co.edu.udea.compumovil.gr06.shareit.UI.Localizacion;

/**
 * Created by jaime on 17/11/2016.
 */

public class Location {

    public Location(String latitud, String longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    private String latitud;
    private String longitud;
}
