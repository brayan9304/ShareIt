package co.edu.udea.compumovil.gr06.shareit.UI.Localizacion;


import java.util.List;

/**
 * Created by jaime on 17/11/2016.
 */
public interface DirectionFinderListener {
    void onDirectionFinderStart();

    void onDirectionFinderSuccess(List<Route> routes);
}
